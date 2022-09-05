package cn.nyaaar.partridgemngservice.service.ehService.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.common.constants.EhUrl;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.common.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.eh.GalleryBasicInfo;
import cn.nyaaar.partridgemngservice.model.eh.GalleryDetail;
import cn.nyaaar.partridgemngservice.service.*;
import cn.nyaaar.partridgemngservice.service.download.DownloadService;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import cn.nyaaar.partridgemngservice.service.ehService.ehBasic.EhEngine;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author yuegenhua
 * @Version $Id: EhServiceImpl.java, v 0.1 2022-31 14:32 yuegenhua Exp $$
 */
@Service
@Slf4j
public class EhServiceImpl implements EhService {

    private final EhEngine ehEngine;
    private final DownloadService downloadService;
    private final EhentaiGalleryService ehentaiGalleryService;
    private final EleFileService eleFileService;
    private final ElementService elementService;
    private final TagService tagService;
    private final EleTagReService eleTagReService;

    private final Map<Long, DownloadingGallery> downloadingGalleryQueue = new HashMap<>();

    public EhServiceImpl(EhEngine ehEngine,
                         DownloadService downloadService,
                         EhentaiGalleryService ehentaiGalleryService,
                         EleFileService eleFileService,
                         ElementService elementService,
                         TagService tagService, EleTagReService eleTagReService) {
        this.ehEngine = ehEngine;
        this.downloadService = downloadService;
        this.ehentaiGalleryService = ehentaiGalleryService;
        this.eleFileService = eleFileService;
        this.elementService = elementService;
        this.tagService = tagService;
        this.eleTagReService = eleTagReService;
    }

    @Override
    public String getGalleryPage(long gid, int pageIndex) {
        EhentaiGallery ehentaiGallery = ehentaiGalleryService.findById(gid);
        BusinessExceptionEnum.GALLERY_NOT_FOUND.assertNotNull(ehentaiGallery);
        EleFile eleFile = eleFileService.getOne(
                new LambdaQueryWrapper<EleFile>()
                        .eq(EleFile::getEleId, ehentaiGallery.getEleId())
                        .eq(EleFile::getPageNum, pageIndex));
        BusinessExceptionEnum.PAGE_NOT_FOUND.assertNotNull(eleFile);
        return FileUtil.file2Base64(new File(eleFile.getPath()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadGallery(long gid, String gtoken) {
        EhentaiGallery ehentaiGallery = getEhGAndSavOrUpdEhg(gid, gtoken);
        List<String> galleryTokens = ehEngine.getPTokens(gid, gtoken);

        DownloadingGallery downloadingGallery = new DownloadingGallery()
                .setGid(ehentaiGallery.getGid())
                .setPages(ehentaiGallery.getPages());
        downloadingGalleryQueue.put(ehentaiGallery.getGid(), downloadingGallery);
        for (int i = 0; i < galleryTokens.size(); i++) {
            downloadGalleryPage(gid, gtoken, ehentaiGallery.getEleId(), galleryTokens.get(i), i, downloadingGallery);
        }
        ehentaiGalleryService.updateData(ehentaiGallery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    // FIXME index gtoken
    public void downloadGalleryPages(long gid, String gtoken, List<Integer> pageIndexes) {
        EhentaiGallery ehentaiGallery = getEhGAndSavOrUpdEhg(gid, gtoken);
        List<String> galleryTokens = ehEngine.getPTokens(gid, gtoken);
        for (Integer index : pageIndexes) {
            // TODO download to cache dir or turn to sync
            // now it's broken
            downloadGalleryPage(gid, gtoken, ehentaiGallery.getEleId(), galleryTokens.get(index), index,
                    null);
        }
    }


    @Override
    public GalleryBasicInfo getGalleryBasicByGid(long gid) {
        EhentaiGallery ehentaiGallery = ehentaiGalleryService.findById(gid);
        BusinessExceptionEnum.GALLERY_NOT_FOUND.assertNotNull(ehentaiGallery);
        GalleryBasicInfo basicInfo = new GalleryBasicInfo();
        BeanUtil.copyProperties(ehentaiGallery, basicInfo);
        List<EleTagRe> eleTagRes = eleTagReService.findList(new EleTagRe().setEleId(basicInfo.getEleId()));
        List<Integer> tagIds = eleTagRes.stream().map(EleTagRe::getTagId).toList();
        List<Tag> tags = tagService.list(new LambdaQueryWrapper<Tag>().in(Tag::getId, tagIds));
        basicInfo.setTags(tags);
        return basicInfo;
    }

    @Override
    public GalleryDetail getGalleryDetailByGid(long gid, String gtoken) {
        GalleryDetail galleryDetail = ehEngine.getGalleryDetail(gid, gtoken);
        EhentaiGallery ehentaiGallery = galleryDetail.transToEntity();
        ehentaiGalleryService.saveOrUpdate(ehentaiGallery);
        return galleryDetail;
    }

    @NotNull
    private EhentaiGallery getEhGAndSavOrUpdEhg(long gid, String gtoken) {
        GalleryDetail galleryDetail = ehEngine.getGalleryDetail(gid, gtoken);
        EhentaiGallery ehentaiGallery = galleryDetail.transToEntity();

        Long eleId = getOrInsertEleIdFromGid(gid);
        downloadGalleryTags(galleryDetail.getTags(), eleId);
        downloadGalleryThumb(gid, galleryDetail.thumb, eleId);
        ehentaiGallery.setEleId(eleId);
        ehentaiGalleryService.saveOrUpdate(ehentaiGallery);
        return ehentaiGallery;
    }

    private void downloadGalleryTags(List<Tag> galleryTags, Long eleId) {
        for (Tag tag : galleryTags) {
            Tag oldTag = tagService.getOne(
                    new LambdaQueryWrapper<Tag>()
                            .eq(Tag::getName, tag.getName())
                            .eq(Tag::getGroupName, tag.getGroupName())
                            .eq(Tag::getSource, tag.getSource()));
            if (oldTag == null) {
                tagService.add(tag);
                EleTagRe eleTagRe = new EleTagRe()
                        .setEleId(eleId)
                        .setTagId(tag.getId());
                eleTagReService.add(eleTagRe);
            } else {
                EleTagRe eleTagRe = new EleTagRe()
                        .setEleId(eleId)
                        .setTagId(oldTag.getId());
                eleTagReService.add(eleTagRe);
            }
        }
    }

    private void downloadGalleryThumb(long gid, String thumbUrl, Long eleId) {
        String folder = PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(), String.valueOf(gid));
        EleFile eleFile = new EleFile();
        eleFile.setEleId(eleId);
        eleFile.setType(FileTypeEnum.jpg.getCode());
        eleFile.setName((0) + FileTypeEnum.jpg.getSuffix());
        eleFile.setPath(PathUtil.simpleConcatUrl(folder, eleFile.getName()));
        eleFile.setPageNum(0);
        eleFile.setIsAvailableFlag(1);
        downloadService.downloadUrlToDest(thumbUrl,
                folder,
                eleFile.getName(),
                null,
                null);
        eleFileService.add(eleFile);
    }

    private Long getOrInsertEleIdFromGid(long gid) {
        Long eleId;
        EhentaiGallery ehentaiGalleryOld = ehentaiGalleryService.getOne(
                new LambdaQueryWrapper<EhentaiGallery>().eq(EhentaiGallery::getGid, gid));
        if (ehentaiGalleryOld == null) {
            Element element = new Element();
            element.setType(SourceEnum.Ehentai.getCode());
            elementService.add(element);
            eleId = element.getId();
        } else {
            eleId = ehentaiGalleryOld.getEleId();
        }
        return eleId;
    }

    private void downloadGalleryPage(long gid, String gtoken, Long eleId, String pToken, int index,
                                     DownloadingGallery downloadingGallery) {
        String folder = PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(), String.valueOf(gid));
        EleFile eleFile = new EleFile();
        eleFile.setEleId(eleId);
        eleFile.setType(FileTypeEnum.jpg.getCode());
        eleFile.setName((index + 1) + FileTypeEnum.jpg.getSuffix());
        eleFile.setPath(PathUtil.simpleConcatUrl(folder, eleFile.getName()));
        eleFile.setPageNum(index + 1);
        eleFile.setIsAvailableFlag(1);

        String pageUrl = EhUrl.getPageUrl(gid, index, pToken);
        String pagePicUrl = ehEngine.getGalleryPage(pageUrl, gid, gtoken).imageUrl;
        downloadService.downloadUrlToDest(pagePicUrl,
                folder,
                eleFile.getName(),
                () -> handlePageDownloadComplete(gid, downloadingGallery),
                () -> handlePageDownloadFail(gid, gtoken, eleId, pToken, index, downloadingGallery));
        eleFileService.saveOrUpdate(eleFile);
    }

    private void handlePageDownloadFail(long gid, String gtoken, Long eleId, String pToken, int index, DownloadingGallery downloadingGallery) {
        Integer failCount = downloadingGallery.downloadFailPageIndex.getOrDefault(index, 0);
        if (failCount > 2) {
            log.warn("[{}]gallery page download failed...index:{}, fail count:{}, try again...", gid, index, failCount);
            downloadingGallery.downloadFailPageIndex.remove(index);
            handlePageDownloadComplete(gid, downloadingGallery);
        } else {
            log.warn("[{}]gallery page download failed...index:{}, fail count:{}", gid, index, failCount + 1);
            downloadingGallery.downloadFailPageIndex.put(index, failCount + 1);
            downloadGalleryPage(gid, gtoken, eleId, pToken, index, downloadingGallery);
        }
    }

    private void handlePageDownloadComplete(long gid, DownloadingGallery downloadingGallery) {
        Integer completeNum = downloadingGallery.downloadCompleteNum.addAndGet(1);
        if (completeNum.equals(downloadingGallery.pages)) {
            downloadingGalleryQueue.remove(gid);

            ehentaiGalleryService.update(new LambdaUpdateWrapper<EhentaiGallery>()
                    .eq(EhentaiGallery::getGid, gid)
                    .set(EhentaiGallery::getDownloadFlag, 1));
            log.info("[{}]gallery download complete!", gid);
        }
    }

    @Data
    @Accessors(chain = true)
    public static class DownloadingGallery {
        long gid;

        int pages;

        Date deadline = DateUtil.date().offset(DateField.HOUR, 1);

        AtomicInteger downloadCompleteNum = new AtomicInteger();

        Map<Integer, Integer> downloadFailPageIndex = new HashMap<>();
    }

    @Override
    public Map<Long, DownloadingGallery> getDownloadingQueue() {
        DateTime now = DateUtil.date();
        List<Long> deadGalleries = new ArrayList<>();
        for (DownloadingGallery downloadingGallery : downloadingGalleryQueue.values()) {
            if (downloadingGallery.deadline.before(now)) {
                deadGalleries.add(downloadingGallery.gid);
            }
        }
        if (!deadGalleries.isEmpty()) {
            for (Long gid : deadGalleries) {
                downloadingGalleryQueue.remove(gid);
            }
        }
        return downloadingGalleryQueue;
    }

}
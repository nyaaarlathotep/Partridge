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
import cn.nyaaar.partridgemngservice.model.eh.GalleryPage;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.service.*;
import cn.nyaaar.partridgemngservice.service.download.DownloadService;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import cn.nyaaar.partridgemngservice.service.ehService.ehBasic.EhEngine;
import cn.nyaaar.partridgemngservice.service.tag.TagService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * index 与 pageIndex 为不同的值，index 和 逻辑页码
 *
 * @author nyaaar
 * @Version $Id: EhServiceImpl.java, v 0.1 2022-31 14:32 nyaaar Exp $$
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

    private final Map<Long, DownloadingGallery> downloadingGalleryQueue = new HashMap<>();

    public EhServiceImpl(EhEngine ehEngine,
                         DownloadService downloadService,
                         EhentaiGalleryService ehentaiGalleryService,
                         EleFileService eleFileService,
                         ElementService elementService,
                         TagService tagService) {
        this.ehEngine = ehEngine;
        this.downloadService = downloadService;
        this.ehentaiGalleryService = ehentaiGalleryService;
        this.eleFileService = eleFileService;
        this.elementService = elementService;
        this.tagService = tagService;
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
    public void downloadGallery(long gid, String gtoken) {
        EhentaiGallery ehentaiGallery = getEhGAndSavOrUpdEhg(gid, gtoken);
        List<String> galleryTokens = ehEngine.getPTokens(gid, gtoken);

        DownloadingGallery downloadingGallery = new DownloadingGallery()
                .setGid(ehentaiGallery.getGid())
                .setGtoken(gtoken)
                .setPages(ehentaiGallery.getPages())
                .setEleId(ehentaiGallery.getEleId());
        downloadingGalleryQueue.put(ehentaiGallery.getGid(), downloadingGallery);
        for (int i = 0; i < galleryTokens.size(); i++) {
            downloadGalleryPageAsync(galleryTokens.get(i), i, downloadingGallery);
        }
        ehentaiGalleryService.saveOrUpdate(ehentaiGallery);
    }

    @Override
    public List<GalleryPage> downloadGalleryPages(long gid, String gtoken, List<Integer> pageIndexes) {
        EhentaiGallery ehentaiGallery = getEhGAndSavOrUpdEhg(gid, gtoken);
        List<GalleryPage> pages = new ArrayList<>();
        for (Integer pageIndex : pageIndexes) {
            int index = pageIndex - 1;
            if (index < 0 || index >= ehentaiGallery.getPages()) {
                BusinessExceptionEnum.FIELD_ER_WITH_ER_VALUE.assertFail("页码 ", pageIndex);
            }
            int ptokenPageIndex = index / 40;
            int ptokenListIndex = index % 40;

            List<String> galleryTokens = ehEngine.getPTokens(gid, gtoken, ptokenPageIndex);
            GalleryPage galleryPage = downloadGalleryPageSync(galleryTokens.get(ptokenListIndex), index, gid, gtoken);
            galleryPage.setPageIndex(pageIndex);
            pages.add(galleryPage);
        }
        return pages;
    }

    @Override
    public List<GalleryPage> downloadGalleryPages(long gid, List<Integer> pageIndexes) {
        return null;
    }

    @Override
    public GalleryBasicInfo getGalleryBasicByGid(long gid) {
        EhentaiGallery ehentaiGallery = ehentaiGalleryService.findById(gid);
        BusinessExceptionEnum.GALLERY_NOT_FOUND.assertNotNull(ehentaiGallery);
        return getGalleryBasicInfo(ehentaiGallery);
    }


    @Override
    public ListResp<GalleryBasicInfo> getGalleryList(int pageIndex) {
        Page<EhentaiGallery> page = new Page<>(pageIndex, 10);
        ehentaiGalleryService.page(page, new LambdaQueryWrapper<EhentaiGallery>().orderByDesc(EhentaiGallery::getEleId));

        return getGalleryBasicInfoListResp(page);
    }

    @Override
    public ListResp<GalleryBasicInfo> getGalleryList(String name, int pageIndex) {
        Page<EhentaiGallery> page = new Page<>(pageIndex, 10);
        ehentaiGalleryService.page(page, new LambdaQueryWrapper<EhentaiGallery>().
                like(EhentaiGallery::getTitle, name).
                or().
                like(EhentaiGallery::getTitleJpn, name).
                orderByDesc(EhentaiGallery::getEleId));

        return getGalleryBasicInfoListResp(page);
    }

    private ListResp<GalleryBasicInfo> getGalleryBasicInfoListResp(Page<EhentaiGallery> page) {
        List<GalleryBasicInfo> galleryBasicInfos = new ArrayList<>();
        for (EhentaiGallery ehentaiGallery : page.getRecords()) {
            GalleryBasicInfo galleryBasicInfo = getGalleryBasicInfo(ehentaiGallery);
            galleryBasicInfos.add(galleryBasicInfo);
        }

        return new ListResp<GalleryBasicInfo>()
                .setList(galleryBasicInfos)
                .setPages(page.getPages())
                .setCurrent(page.getCurrent());
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
        tagService.saveOrUpdateTagInfo(galleryDetail.getTags(), eleId);
        downloadGalleryThumb(gid, galleryDetail.thumb, eleId);
        ehentaiGallery.setEleId(eleId);
        ehentaiGalleryService.saveOrUpdate(ehentaiGallery);
        return ehentaiGallery;
    }

    @NotNull
    private GalleryBasicInfo getGalleryBasicInfo(EhentaiGallery ehentaiGallery) {
        GalleryBasicInfo basicInfo = new GalleryBasicInfo();
        BeanUtil.copyProperties(ehentaiGallery, basicInfo);
        basicInfo.setTags(tagService.getTagInfos(basicInfo.getEleId()));
        return basicInfo;
    }


    private void downloadGalleryThumb(long gid, String thumbUrl, Long eleId) {
        String folder = PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(), String.valueOf(gid));
        FileTypeEnum fileTypeEnum = FileTypeEnum.getTypeBySuffix(thumbUrl);
        EleFile eleFile = createEleFile(eleId, 0, folder, fileTypeEnum);
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

    private void downloadGalleryPageAsync(String pToken, int index,
                                          DownloadingGallery downloadingGallery) {
        long gid = downloadingGallery.gid;
        long eleId = downloadingGallery.eleId;
        String gtoken = downloadingGallery.gtoken;
        int pageIndex = index + 1;
        String folder = PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(), String.valueOf(gid));

        String pageUrl = EhUrl.getPageUrl(gid, index, pToken);
        String pagePicUrl = ehEngine.getGalleryPage(pageUrl, gid, gtoken).imageUrl;
        FileTypeEnum fileTypeEnum = FileTypeEnum.getTypeBySuffix(pagePicUrl);

        EleFile eleFile = createEleFile(eleId, pageIndex, folder, fileTypeEnum);
        downloadService.downloadUrlToDest(pagePicUrl,
                folder,
                eleFile.getName(),
                () -> handlePageDownloadComplete(downloadingGallery),
                () -> handlePageDownloadFail(pToken, index, downloadingGallery));
        eleFileService.saveOrUpdate(eleFile);
    }

    @NotNull
    private static EleFile createEleFile(long eleId, int pageIndex, String folder, FileTypeEnum fileTypeEnum) {
        EleFile eleFile = new EleFile();
        eleFile.setEleId(eleId);
        eleFile.setName(pageIndex + fileTypeEnum.getSuffix());
        eleFile.setPath(PathUtil.simpleConcatUrl(folder, eleFile.getName()));
        eleFile.setPageNum(pageIndex);
        eleFile.setIsAvailableFlag(1);
        eleFile.setType(fileTypeEnum.getCode());
        return eleFile;
    }

    private GalleryPage downloadGalleryPageSync(String pToken, int index, long gid, String gtoken) {

        String pageUrl = EhUrl.getPageUrl(gid, index, pToken);
        String pagePicUrl = ehEngine.getGalleryPage(pageUrl, gid, gtoken).imageUrl;
        FileTypeEnum fileTypeEnum = FileTypeEnum.getTypeBySuffix(pagePicUrl);
        return new GalleryPage()
                .setPageBase64(downloadService.downloadUrlToBase64(pagePicUrl))
                .setFileSuffix(fileTypeEnum.getSuffix());
    }

    private void handlePageDownloadFail(String pToken, int index, DownloadingGallery downloadingGallery) {
        long gid = downloadingGallery.gid;
        Integer failCount = downloadingGallery.downloadFailPageIndex.getOrDefault(index, 0);
        if (failCount > 2) {
            log.warn("[{}]gallery page download failed...index:{}, fail count:{}", gid, index, failCount);
            downloadingGallery.downloadFailPageIndex.remove(index);
            handlePageDownloadComplete(downloadingGallery);
        } else {
            log.warn("[{}]gallery page download failed...index:{}, fail count:{}, try again...", gid, index, failCount + 1);
            downloadingGallery.downloadFailPageIndex.put(index, failCount + 1);
            downloadGalleryPageAsync(pToken, index, downloadingGallery);
        }
    }

    private void handlePageDownloadComplete(DownloadingGallery downloadingGallery) {
        long gid = downloadingGallery.gid;
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
        String gtoken;
        long eleId;
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
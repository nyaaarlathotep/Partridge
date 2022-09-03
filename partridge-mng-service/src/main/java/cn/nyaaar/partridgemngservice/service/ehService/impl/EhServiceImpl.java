package cn.nyaaar.partridgemngservice.service.ehService.impl;

import cn.nyaaar.partridgemngservice.constants.EhUrl;
import cn.nyaaar.partridgemngservice.constants.Settings;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.eh.GalleryDetail;
import cn.nyaaar.partridgemngservice.service.*;
import cn.nyaaar.partridgemngservice.service.download.DownloadService;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import cn.nyaaar.partridgemngservice.service.ehService.ehBasic.EhEngine;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: EhServiceImpl.java, v 0.1 2022-31 14:32 yuegenhua Exp $$
 */
@Service
public class EhServiceImpl implements EhService {

    private final EhEngine ehEngine;
    private final DownloadService downloadService;
    private final EhentaiGalleryService ehentaiGalleryService;
    private final EleFileService eleFileService;
    private final ElementService elementService;
    private final TagService tagService;
    private final EleTagReService eleTagReService;

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
        for (int i = 0; i < galleryTokens.size(); i++) {
            downloadGalleryPage(gid, gtoken, ehentaiGallery.getEleId(), galleryTokens.get(i), i);
        }
        ehentaiGallery.setDownloadFlag(1);
        ehentaiGalleryService.updateData(ehentaiGallery);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadGalleryPages(long gid, String gtoken, List<Integer> pageIndexes) {
        EhentaiGallery ehentaiGallery = getEhGAndSavOrUpdEhg(gid, gtoken);
        List<String> galleryTokens = ehEngine.getPTokens(gid, gtoken);
        for (Integer index : pageIndexes) {
            downloadGalleryPage(gid, gtoken, ehentaiGallery.getEleId(), galleryTokens.get(index), index);
        }
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
                eleFile.getName());
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

    private void downloadGalleryPage(long gid, String gtoken, Long eleId, String pToken, int index) {
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
                eleFile.getName());
        eleFileService.add(eleFile);
    }

}
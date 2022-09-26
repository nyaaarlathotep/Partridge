package cn.nyaaar.partridgemngservice.service.ehService.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.TagDto;
import cn.nyaaar.partridgemngservice.model.eh.*;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.service.*;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import cn.nyaaar.partridgemngservice.service.ehService.ehBasic.EhDownload;
import cn.nyaaar.partridgemngservice.service.ehService.ehBasic.EhEngine;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

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
    private final EhDownload ehDownload;
    private final EhentaiGalleryService ehentaiGalleryService;
    private final EleFileService eleFileService;
    private final ElementService elementService;
    private final TagInfoService tagInfoService;

    public EhServiceImpl(EhEngine ehEngine,
                         EhentaiGalleryService ehentaiGalleryService,
                         EleFileService eleFileService,
                         ElementService elementService,
                         TagInfoService tagInfoService, EhDownload ehDownload) {
        this.ehEngine = ehEngine;
        this.ehentaiGalleryService = ehentaiGalleryService;
        this.eleFileService = eleFileService;
        this.elementService = elementService;
        this.tagInfoService = tagInfoService;
        this.ehDownload = ehDownload;
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
    public List<GalleryPage> getGalleryPage(long eleId, List<Integer> pageIndexes) {
        if (pageIndexes.isEmpty()) {
            return new ArrayList<>();
        }
        List<EleFile> eleFiles = eleFileService.list(new LambdaQueryWrapper<EleFile>()
                .eq(EleFile::getEleId, eleId)
                .in(EleFile::getPageNum, pageIndexes));

        return eleFiles.stream().map(eleFileService::getGalleryPage).toList();
    }

    @Override
    public void downloadGallery(long gid, String gtoken, String userName) {
        EhentaiGallery ehentaiGallery = getEhGAndSavOrUpdEhg(gid, gtoken, true);
        ehDownload.downloadGalleryAsync(ehentaiGallery);
    }

    @Override
    public List<GalleryPage> downloadGalleryPages(long gid, String gtoken, List<Integer> pageIndexes) {
        EhentaiGallery ehentaiGallery = getEhGAndSavOrUpdEhg(gid, gtoken, false);
        return ehDownload.getGalleryPages(gid, gtoken, pageIndexes, ehentaiGallery);
    }

    @Override
    public GalleryBasicInfo getGalleryBasicByGid(long eleId) {
        EhentaiGallery ehentaiGallery = ehentaiGalleryService.getOne(new LambdaQueryWrapper<EhentaiGallery>().
                eq(EhentaiGallery::getEleId, eleId));
        BusinessExceptionEnum.GALLERY_NOT_FOUND.assertNotNull(ehentaiGallery);
        return getGalleryBasicInfo(ehentaiGallery);
    }


    @Override
    public ListResp<GalleryBasicInfo> getGalleryList(int pageIndex) {
        Page<EhentaiGallery> page = new Page<>(pageIndex, Settings.getPageSize());
        ehentaiGalleryService.page(page, new LambdaQueryWrapper<EhentaiGallery>().orderByDesc(EhentaiGallery::getEleId));

        return getGalleryBasicInfoListResp(page);
    }

    @Override
    public ListResp<GalleryBasicInfo> getGalleryList(GalleryQuery galleryQuery, int pageIndex) {
        Page<EhentaiGallery> page = new Page<>(pageIndex, Settings.getPageSize());
        LambdaQueryWrapper<EhentaiGallery> ehentaiGalleryLambdaQueryWrapper = getQueryWrapper(galleryQuery);
        queryPage(galleryQuery, page, ehentaiGalleryLambdaQueryWrapper);
        return getGalleryBasicInfoListResp(page);
    }

    private void queryPage(GalleryQuery galleryQuery, Page<EhentaiGallery> page, LambdaQueryWrapper<EhentaiGallery> ehentaiGalleryLambdaQueryWrapper) {
        List<Integer> tagInfoIds = null;
        if (Objects.nonNull(galleryQuery.getTagDtos()) && !galleryQuery.getTagDtos().isEmpty()) {
            tagInfoIds = galleryQuery.getTagDtos()
                    .parallelStream()
                    .map(queryTagInfo -> tagInfoService.getOne(Wrappers.query(queryTagInfo.transToEntity())))
                    .filter(Objects::nonNull)
                    .map(TagInfo::getId)
                    .toList();
        }
        ehentaiGalleryService.pageWithTag(page, ehentaiGalleryLambdaQueryWrapper, tagInfoIds);
    }

    private ListResp<GalleryBasicInfo> getGalleryBasicInfoListResp(Page<EhentaiGallery> page) {
        List<GalleryBasicInfo> galleryBasicInfos = page.getRecords()
                .parallelStream()
                .map(this::getGalleryBasicInfo)
                .toList();

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
    private EhentaiGallery getEhGAndSavOrUpdEhg(long gid, String gtoken, boolean downloadThumb) {
        GalleryDetail galleryDetail = ehEngine.getGalleryDetail(gid, gtoken);
        EhentaiGallery ehentaiGallery = galleryDetail.transToEntity();

        Long eleId = getOrInsertEleIdFromGid(gid);
        tagInfoService.saveOrUpdateTagInfoWithRe(galleryDetail.getTags(), eleId);
        if (downloadThumb) {
            ehDownload.downloadGalleryThumb(gid, galleryDetail.thumb, eleId, ehentaiGallery.getTitle());
        }
        ehentaiGallery.setEleId(eleId);
        ehentaiGalleryService.saveOrUpdate(ehentaiGallery);
        return ehentaiGallery;
    }


    private Long getOrInsertEleIdFromGid(long gid) {
        Long eleId;
        EhentaiGallery ehentaiGalleryOld = ehentaiGalleryService.getOne(
                new LambdaQueryWrapper<EhentaiGallery>().eq(EhentaiGallery::getGid, gid));
        if (ehentaiGalleryOld == null) {
            Element element = new Element();
            element.setType(SourceEnum.Ehentai.getCode());
            element.setAvailableFlag(PrConstant.VALIDATED);
            elementService.save(element);
            eleId = element.getId();
        } else {
            eleId = ehentaiGalleryOld.getEleId();
        }
        return eleId;
    }


    @NotNull
    private GalleryBasicInfo getGalleryBasicInfo(EhentaiGallery ehentaiGallery) {
        GalleryBasicInfo basicInfo = new GalleryBasicInfo();
        BeanUtil.copyProperties(ehentaiGallery, basicInfo);
        List<TagInfo> tagInfos = tagInfoService.getTagInfos(basicInfo.getEleId());
        basicInfo.setTags(tagInfos.stream().map(TagDto::new).toList());
        return basicInfo;
    }

    @Override
    public Map<Long, DownloadingGallery> getDownloadingQueue() {
        return ehDownload.getDownloadingQueue();
    }

    private static LambdaQueryWrapper<EhentaiGallery> getQueryWrapper(GalleryQuery galleryQuery) {
        LambdaQueryWrapper<EhentaiGallery> ehentaiGalleryLambdaQueryWrapper = Wrappers.lambdaQuery(EhentaiGallery.class);
        if (galleryQuery.getGid() != null && galleryQuery.getGid() != 0) {
            ehentaiGalleryLambdaQueryWrapper.eq(EhentaiGallery::getGid, galleryQuery.getGid());
            return ehentaiGalleryLambdaQueryWrapper;
        }
        if (galleryQuery.getEleId() != null && galleryQuery.getEleId() != 0) {
            ehentaiGalleryLambdaQueryWrapper.eq(EhentaiGallery::getEleId, galleryQuery.getEleId());
            return ehentaiGalleryLambdaQueryWrapper;
        }
        if (StringUtils.isNotEmpty(galleryQuery.getTitle())) {
            ehentaiGalleryLambdaQueryWrapper.like(EhentaiGallery::getTitle, galleryQuery.getTitle());
        }
        if (StringUtils.isNotEmpty(galleryQuery.getTitleJpn())) {
            ehentaiGalleryLambdaQueryWrapper.like(EhentaiGallery::getTitleJpn, galleryQuery.getTitleJpn());
        }
        if (galleryQuery.getCategory() != null) {
            ehentaiGalleryLambdaQueryWrapper.eq(EhentaiGallery::getCategory, galleryQuery.getCategory());
        }
        if (StringUtils.isNotEmpty(galleryQuery.getUploader())) {
            ehentaiGalleryLambdaQueryWrapper.like(EhentaiGallery::getUploader, galleryQuery.getUploader());
        }
        if (galleryQuery.getRatingCeiling() != null) {
            ehentaiGalleryLambdaQueryWrapper.le(EhentaiGallery::getRating, galleryQuery.getRatingCeiling());
        }
        if (galleryQuery.getRatingFloor() != null) {
            ehentaiGalleryLambdaQueryWrapper.ge(EhentaiGallery::getRating, galleryQuery.getRatingFloor());
        }
        if (galleryQuery.getRatingCountCeiling() != null) {
            ehentaiGalleryLambdaQueryWrapper.le(EhentaiGallery::getRatingCount, galleryQuery.getRatingCountCeiling());
        }
        if (galleryQuery.getRatingCountFloor() != null) {
            ehentaiGalleryLambdaQueryWrapper.ge(EhentaiGallery::getRatingCount, galleryQuery.getRatingCountFloor());
        }
        if (galleryQuery.getPagesCeiling() != null) {
            ehentaiGalleryLambdaQueryWrapper.le(EhentaiGallery::getPages, galleryQuery.getPagesCeiling());
        }
        if (galleryQuery.getPagesFloor() != null) {
            ehentaiGalleryLambdaQueryWrapper.ge(EhentaiGallery::getRating, galleryQuery.getPagesFloor());
        }
        if (galleryQuery.getPostedStart() != null) {
            ehentaiGalleryLambdaQueryWrapper.ge(EhentaiGallery::getPosted, galleryQuery.getPostedStart());
        }
        if (galleryQuery.getPostedEnd() != null) {
            ehentaiGalleryLambdaQueryWrapper.le(EhentaiGallery::getPosted, galleryQuery.getPostedEnd());
        }
        if (galleryQuery.getFavoriteCountCeiling() != null) {
            ehentaiGalleryLambdaQueryWrapper.le(EhentaiGallery::getFavoriteCount, galleryQuery.getFavoriteCountCeiling());
        }
        if (galleryQuery.getFavoriteCountFloor() != null) {
            ehentaiGalleryLambdaQueryWrapper.ge(EhentaiGallery::getFavoriteCount, galleryQuery.getFavoriteCountFloor());
        }
        if (galleryQuery.getCashedFlag() != null) {
            ehentaiGalleryLambdaQueryWrapper.eq(EhentaiGallery::getCashedFlag, galleryQuery.getCashedFlag());
        }
        if (galleryQuery.getDownloadFlag() != null) {
            ehentaiGalleryLambdaQueryWrapper.ge(EhentaiGallery::getDownloadFlag, galleryQuery.getDownloadFlag());
        }
        if (galleryQuery.getOrderByEleID() != null && galleryQuery.getOrderByEleID()) {
            ehentaiGalleryLambdaQueryWrapper.orderByDesc(EhentaiGallery::getEleId);
        } else {
            ehentaiGalleryLambdaQueryWrapper.orderByDesc(EhentaiGallery::getGid);
        }

        return ehentaiGalleryLambdaQueryWrapper;
    }
}
package cn.nyaaar.partridgemngservice.service.ehService.impl;

import cn.nyaaar.partridgemngservice.constants.EhUrl;
import cn.nyaaar.partridgemngservice.constants.Settings;
import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.entity.File;
import cn.nyaaar.partridgemngservice.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.model.eh.GalleryDetail;
import cn.nyaaar.partridgemngservice.service.EhentaiGalleryService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.FileService;
import cn.nyaaar.partridgemngservice.service.download.DownloadService;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import cn.nyaaar.partridgemngservice.service.ehService.ehBasic.EhEngine;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final FileService fileService;
    private final ElementService elementService;

    public EhServiceImpl(EhEngine ehEngine, DownloadService downloadService, EhentaiGalleryService ehentaiGalleryService, FileService fileService, ElementService elementService) {
        this.ehEngine = ehEngine;
        this.downloadService = downloadService;
        this.ehentaiGalleryService = ehentaiGalleryService;
        this.fileService = fileService;
        this.elementService = elementService;
    }

    @Override
    public String getGalleryPage(long gid, int pageIndex) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadGallery(long gid, String gtoken) {
        EhentaiGallery ehentaiGallery = getEhGAndSavOrUpdEhg(gid, gtoken);
        List<String> galleryTokens = ehEngine.getPTokens(gid, gtoken);
        for (int i = 0; i < galleryTokens.size(); i++) {
            downloadUrlAndInsertFile(gid, gtoken, ehentaiGallery.getEleId(), galleryTokens, i);
        }
    }

    @NotNull
    private EhentaiGallery getEhGAndSavOrUpdEhg(long gid, String gtoken) {
        GalleryDetail galleryDetail = ehEngine.getGalleryDetail(gid, gtoken);
        EhentaiGallery ehentaiGallery = new EhentaiGallery(galleryDetail);

        EhentaiGallery ehentaiGalleryOld = ehentaiGalleryService.getOne(
                new LambdaQueryWrapper<EhentaiGallery>().eq(EhentaiGallery::getGid, gid));
        if (ehentaiGalleryOld == null) {
            Element element = new Element();
            element.setType(SourceEnum.Ehentai.getCode());
            elementService.add(element);
            ehentaiGallery.setEleId(element.getId());
        } else {
            ehentaiGallery.setEleId(ehentaiGalleryOld.getEleId());
        }
        ehentaiGalleryService.saveOrUpdate(ehentaiGallery);
        return ehentaiGallery;
    }

    private void downloadUrlAndInsertFile(long gid, String gtoken, Long eleId, List<String> galleryTokens, int i) {
        File file = new File();
        file.setEleId(eleId);
        file.setType(FileTypeEnum.jpg.getCode());
        file.setName((i + 1) + FileTypeEnum.jpg.getEnd());
        file.setPath(PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(), String.valueOf(gid), file.getName()));
        file.setIsAvailableFlag(1);

        String pageUrl = EhUrl.getPageUrl(gid, i, galleryTokens.get(i));
        String pagePicUrl = ehEngine.getGalleryPage(pageUrl, gid, gtoken).imageUrl;
        downloadService.downloadUrlToDest(pagePicUrl,
                PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(), String.valueOf(gid)),
                file.getName());
        fileService.add(file);
    }

}
package cn.nyaaar.partridgemngservice.service.ehService.impl;

import cn.nyaaar.partridgemngservice.constants.EhUrl;
import cn.nyaaar.partridgemngservice.constants.Settings;
import cn.nyaaar.partridgemngservice.service.download.DownloadService;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import cn.nyaaar.partridgemngservice.service.ehService.ehBasic.EhEngine;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yuegenhua
 * @Version $Id: EhServiceImpl.java, v 0.1 2022-31 14:32 yuegenhua Exp $$
 */
@Service
public class EhServiceImpl implements EhService {

    private final EhEngine ehEngine;
    private final DownloadService downloadService;


    public EhServiceImpl(EhEngine ehEngine, DownloadService downloadService) {
        this.ehEngine = ehEngine;
        this.downloadService = downloadService;
    }

    @Override
    public String getGalleryPage(long gid, int pageIndex) {

        return null;
    }

    @Override
    public void downloadGallery(long gid, String gtoken) {
        List<String> galleryTokens = ehEngine.getPTokens(gid, gtoken);

        for (int i = 0; i < galleryTokens.size(); i++) {

            String pageUrl = EhUrl.getPageUrl(gid, i, galleryTokens.get(i));
            String pagePicUrl = ehEngine.getGalleryPage(pageUrl, gid, gtoken).imageUrl;
            downloadService.downloadUrlToDest(pagePicUrl,
                    PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(), String.valueOf(gid)),
                    (i + 1) + ".jpg");
        }
    }

}
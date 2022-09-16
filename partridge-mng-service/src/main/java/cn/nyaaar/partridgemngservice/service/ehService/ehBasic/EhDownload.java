package cn.nyaaar.partridgemngservice.service.ehService.ehBasic;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.common.constants.EhUrl;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.common.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import cn.nyaaar.partridgemngservice.entity.EleFile;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.eh.DownloadingGallery;
import cn.nyaaar.partridgemngservice.model.eh.GalleryPage;
import cn.nyaaar.partridgemngservice.service.EhentaiGalleryService;
import cn.nyaaar.partridgemngservice.service.EleFileService;
import cn.nyaaar.partridgemngservice.service.download.DownloadService;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yuegenhua
 * @Version $Id: EhDownload.java, v 0.1 2022-16 10:42 yuegenhua Exp $$
 */
@Slf4j
@Service
public class EhDownload {
    private final Map<Long, DownloadingGallery> downloadingGalleryQueue = new HashMap<>();
    private final EhEngine ehEngine;
    private final DownloadService downloadService;
    private final EleFileService eleFileService;
    private final EhentaiGalleryService ehentaiGalleryService;
    private final ThreadPoolTaskExecutor downloadQueueExecutor;

    private static final int ehentaiPreviewSize = 40;
    private static final int maxFailCount = 2;

    public EhDownload(EhEngine ehEngine,
                      DownloadService downloadService,
                      EleFileService eleFileService,
                      EhentaiGalleryService ehentaiGalleryService,
                      ThreadPoolTaskExecutor downloadQueueExecutor) {
        this.ehEngine = ehEngine;
        this.downloadService = downloadService;
        this.eleFileService = eleFileService;
        this.ehentaiGalleryService = ehentaiGalleryService;
        this.downloadQueueExecutor = downloadQueueExecutor;
    }

    public void downloadGalleryAsync(@NonNull EhentaiGallery ehentaiGallery) {
        BusinessExceptionEnum.NOT_EXISTS.assertNotNull(ehentaiGallery.getGid(), "gid");
        BusinessExceptionEnum.NOT_EXISTS.assertNotNull(ehentaiGallery.getToken(), "gtoken");
        BusinessExceptionEnum.NOT_EXISTS.assertNotNull(ehentaiGallery.getPages(), "pages");
        BusinessExceptionEnum.NOT_EXISTS.assertNotNull(ehentaiGallery.getEleId(), "eleId");

        DownloadingGallery downloadingGallery = new DownloadingGallery()
                .setGid(ehentaiGallery.getGid())
                .setGtoken(ehentaiGallery.getToken())
                .setPages(ehentaiGallery.getPages())
                .setEleId(ehentaiGallery.getEleId());
        downloadingGalleryQueue.put(ehentaiGallery.getGid(), downloadingGallery);
        downloadQueueExecutor.submit(() -> {
            List<String> galleryTokens = ehEngine.getPTokens(ehentaiGallery.getGid(), ehentaiGallery.getToken());
            for (int i = 0; i < galleryTokens.size(); i++) {
                downloadGalleryPageAsync(galleryTokens.get(i), i, downloadingGallery);
            }
        });
    }

    private GalleryPage downloadGalleryPageSync(String pToken, int index, long gid, String gtoken) {
        String pageUrl = EhUrl.getPageUrl(gid, index, pToken);
        String pagePicUrl = ehEngine.getGalleryPage(pageUrl, gid, gtoken).imageUrl;
        FileTypeEnum fileTypeEnum = FileTypeEnum.getTypeBySuffix(pagePicUrl);
        return new GalleryPage()
                .setPageBase64(downloadService.downloadUrlToBase64(pagePicUrl))
                .setPageIndex(index + 1)
                .setFileSuffix(fileTypeEnum.getSuffix());
    }

    private void handlePageDownloadFail(String pToken, int index, DownloadingGallery downloadingGallery) {
        long gid = downloadingGallery.getGid();
        Integer failCount = downloadingGallery.getDownloadFailPageIndex().getOrDefault(index, 0);
        if (failCount > maxFailCount) {
            log.warn("[{}]gallery page download failed...index:{}, fail count:{}", gid, index, failCount);
            downloadingGallery.getDownloadFailPageIndex().remove(index);
            handlePageDownloadComplete(downloadingGallery);
        } else {
            log.warn("[{}]gallery page download failed...index:{}, fail count:{}, try again...", gid, index, failCount + 1);
            downloadingGallery.getDownloadFailPageIndex().put(index, failCount + 1);
            downloadGalleryPageAsync(pToken, index, downloadingGallery);
        }
    }

    private void downloadGalleryPageAsync(String pToken, int index,
                                          DownloadingGallery downloadingGallery) {
        long gid = downloadingGallery.getGid();
        long eleId = downloadingGallery.getEleId();
        String gtoken = downloadingGallery.getGtoken();
        int pageIndex = index + 1;
        String folder = PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(), String.valueOf(gid));

        String pageUrl = EhUrl.getPageUrl(gid, index, pToken);
        String pagePicUrl = ehEngine.getGalleryPage(pageUrl, gid, gtoken).imageUrl;
        FileTypeEnum fileTypeEnum = FileTypeEnum.getTypeBySuffix(pagePicUrl);

        EleFile eleFile = createEleFile(eleId, pageIndex, folder, fileTypeEnum);
        downloadService.downloadUrlToDest(pagePicUrl,
                folder,
                eleFile.getName(),
                () -> handlePageDownloadSuccess(eleFile, downloadingGallery),
                () -> handlePageDownloadFail(pToken, index, downloadingGallery));
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

    private void handlePageDownloadSuccess(EleFile eleFile, DownloadingGallery downloadingGallery) {
        eleFileService.saveOrUpdate(eleFile);
        handlePageDownloadComplete(downloadingGallery);
    }

    private void handlePageDownloadComplete(DownloadingGallery downloadingGallery) {
        long gid = downloadingGallery.getGid();
        Integer completeNum = downloadingGallery.getDownloadCompleteNum().addAndGet(1);
        if (completeNum.equals(downloadingGallery.getPages())) {
            downloadingGalleryQueue.remove(gid);

            ehentaiGalleryService.update(new LambdaUpdateWrapper<EhentaiGallery>()
                    .eq(EhentaiGallery::getGid, gid)
                    .set(EhentaiGallery::getDownloadFlag, 1));
            log.info("[{}]gallery download complete!", gid);
        }
    }

    public void downloadGalleryThumb(long gid, String thumbUrl, Long eleId) {
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

    @NotNull
    public List<GalleryPage> getGalleryPages(long gid, String gtoken, List<Integer> pageIndexes, EhentaiGallery ehentaiGallery) {
        List<GalleryPage> pages = new ArrayList<>();
        for (Integer pageIndex : pageIndexes) {
            int index = pageIndex - 1;
            if (index < 0 || index >= ehentaiGallery.getPages()) {
                BusinessExceptionEnum.FIELD_ER_WITH_ER_VALUE.assertFail("页码 ", pageIndex);
            }
            int ptokenPageIndex = index / ehentaiPreviewSize;
            int ptokenListIndex = index % ehentaiPreviewSize;

            List<String> galleryTokens = ehEngine.getPTokens(gid, gtoken, ptokenPageIndex);
            GalleryPage galleryPage = downloadGalleryPageSync(galleryTokens.get(ptokenListIndex), index, gid, gtoken);
            galleryPage.setPageIndex(pageIndex);
            pages.add(galleryPage);
        }
        return pages;
    }

    public Map<Long, DownloadingGallery> getDownloadingQueue() {
        DateTime now = DateUtil.date();
        List<Long> deadGalleries = new ArrayList<>();
        for (DownloadingGallery downloadingGallery : downloadingGalleryQueue.values()) {
            if (downloadingGallery.getDeadline().before(now)) {
                deadGalleries.add(downloadingGallery.getGid());
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
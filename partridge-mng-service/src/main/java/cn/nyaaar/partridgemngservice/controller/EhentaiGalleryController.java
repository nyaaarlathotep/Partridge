package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.model.eh.EhPreviewResp;
import cn.nyaaar.partridgemngservice.model.eh.GalleryBasicInfo;
import cn.nyaaar.partridgemngservice.model.eh.GalleryDetail;
import cn.nyaaar.partridgemngservice.model.eh.EhDownloadReq;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.validate.EhDownload;
import cn.nyaaar.partridgemngservice.model.validate.EhPreview;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * ehentaiController
 *
 * @author nyaaar
 * @Version $Id: EhentaiGalleryController.java, v 0.1 2022-05 14:32 nyaaar Exp $$
 */
@Tag(name = "ehentai")
@RestController
@RequestMapping("/ehentai")
@Slf4j
public class EhentaiGalleryController {

    private EhService ehService;

    @Autowired
    public void DI(EhService ehService) {
        this.ehService = ehService;
    }

    @Operation(summary = "gallery基本信息", description = "通过gid获取gallery被保存在数据库的基本信息")
    @GetMapping(value = "/info/basic")
    public R<GalleryBasicInfo> getGalleryBasic(@RequestParam Long gid) {

        return new R<>(ehService.getGalleryBasicByGid(gid));
    }

    @Operation(summary = "gallery详细信息", description = "通过gid与gtoken请求ehentai获得galleryDetail信息")
    @GetMapping(value = "/info/detail")
    public R<GalleryDetail> getGalleryDetail(@RequestParam Long gid, @RequestParam String gtoken) {

        return new R<>(ehService.getGalleryDetailByGid(gid, gtoken));
    }

    @Operation(summary = "下载gallery", description = "通过gid与gtoken下载对应gallery")
    @PostMapping(value = "/download")
    public R<String> downloadGallery(@RequestBody @Validated(EhDownload.class) EhDownloadReq ehDownloadReq) {
        ehService.downloadGallery(ehDownloadReq.getGid(), ehDownloadReq.getGtoken());
        return new R<>();
    }

    @Operation(summary = "预览gallery", description = "通过gid与gtoken获取对应页")
    @PostMapping(value = "/view")
    public R<EhPreviewResp> previewGallery(@RequestBody @Validated(EhPreview.class) EhDownloadReq ehDownloadReq) {
        EhPreviewResp ehPreviewResp = new EhPreviewResp()
                .setPagesBase64(ehService.downloadGalleryPages(ehDownloadReq.getGid(), ehDownloadReq.getGtoken(), ehDownloadReq.getPageIndexes()));
        return new R<>(ehPreviewResp);
    }
}
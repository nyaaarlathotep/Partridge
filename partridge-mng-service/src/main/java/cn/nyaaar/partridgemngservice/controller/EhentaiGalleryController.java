package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.model.eh.EhPreviewResp;
import cn.nyaaar.partridgemngservice.model.eh.GalleryBasicInfo;
import cn.nyaaar.partridgemngservice.model.eh.GalleryDetail;
import cn.nyaaar.partridgemngservice.model.eh.EhDownloadReq;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.validate.EhDownload;
import cn.nyaaar.partridgemngservice.model.validate.EhPreview;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

    private final EhService ehService;

    public EhentaiGalleryController(EhService ehService) {
        this.ehService = ehService;
    }

    @Operation(summary = "gallery 基本信息", description = "通过 gid 获取 gallery 被保存在数据库的基本信息")
    @GetMapping(value = "/basic")
    public R<GalleryBasicInfo> getGalleryBasic(@RequestParam Long gid) {

        return new R<>(ehService.getGalleryBasicByGid(gid));
    }

    @Operation(summary = "gallery 详细信息", description = "通过 gid 与 gtoken 请求 ehentai 获得 galleryDetail 信息")
    @GetMapping(value = "/detail")
    public R<GalleryDetail> getGalleryDetail(@RequestParam Long gid, @RequestParam String gtoken) {

        return new R<>(ehService.getGalleryDetailByGid(gid, gtoken));
    }

    @Operation(summary = "gallery 基本信息列表", description = "获取保存在数据库的 gallery 基本信息列表")
    @GetMapping(value = "/basic/list/{pageIndex}")
    public R<ListResp<GalleryBasicInfo>> getGalleryBasic(@PathVariable Integer pageIndex) {

        return new R<>(ehService.getGalleryList(pageIndex));
    }

    @Operation(summary = "gallery 基本信息列表", description = "通过 name 模糊搜索 title 或者 titleJpn 的 gallery 基本信息列表")
    @GetMapping(value = "/basic/nameSearch/{pageIndex}")
    public R<ListResp<GalleryBasicInfo>> getGalleryBasic(@RequestParam String name, @PathVariable Integer pageIndex) {

        return new R<>(ehService.getGalleryList(name, pageIndex));
    }

    @Operation(summary = "下载 gallery", description = "通过 gid 与 gtoken 异步下载对应 gallery")
    @PostMapping(value = "/download")
    public R<String> downloadGallery(@RequestBody @Validated(EhDownload.class) EhDownloadReq ehDownloadReq) {
        ehService.downloadGallery(ehDownloadReq.getGid(), ehDownloadReq.getGtoken());
        return new R<>();
    }

    @Operation(summary = "预览 gallery", description = "通过 gid 与 gtoken 获取对应页")
    @PostMapping(value = "/preview")
    public R<EhPreviewResp> previewGallery(@RequestBody @Validated(EhPreview.class) EhDownloadReq ehDownloadReq) {
        EhPreviewResp ehPreviewResp = new EhPreviewResp()
                .setPages(ehService.downloadGalleryPages(ehDownloadReq.getGid(), ehDownloadReq.getGtoken(), ehDownloadReq.getPageIndexes()));
        return new R<>(ehPreviewResp);
    }

    @Operation(summary = "预览 gallery", description = "通过 gid 获得 下载好的画廊对应页")
    @GetMapping(value = "/view")
    public R<EhPreviewResp> getGalleryPages(@RequestBody @Validated(EhPreview.class)  EhDownloadReq ehDownloadReq) {
        // TODO
        EhPreviewResp ehPreviewResp = new EhPreviewResp();
        return new R<>(ehPreviewResp);
    }
}
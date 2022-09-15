package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.model.eh.*;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.validate.EhDownload;
import cn.nyaaar.partridgemngservice.model.validate.EhPreview;
import cn.nyaaar.partridgemngservice.model.validate.EhView;
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

    @Operation(summary = "gallery 基本信息", description = "通过 eleId 获取 gallery 被保存在数据库的基本信息")
    @GetMapping(value = "/basic")
    @LogAnnotation
    public R<GalleryBasicInfo> getGalleryBasic(@RequestParam Long eleId) {

        return new R<>(ehService.getGalleryBasicByGid(eleId));
    }

    @Operation(summary = "预览 gallery", description = "通过 eleId 获得 下载好的画廊对应页")
    @PostMapping(value = "/view")
    @LogAnnotation
    public R<EhViewResp> getGalleryPages(@RequestBody @Validated(EhView.class) EhCommonReq ehCommonReq) {
        EhViewResp ehViewResp = new EhViewResp()
                .setPages(ehService.getGalleryPage(ehCommonReq.getEleId(), ehCommonReq.getPageIndexes()));
        return new R<>(ehViewResp);
    }

    @Operation(summary = "gallery 详细信息", description = "通过 gid 与 gtoken 请求 ehentai 获得 galleryDetail 信息")
    @GetMapping(value = "/detail")
    @LogAnnotation
    public R<GalleryDetail> getGalleryDetail(@RequestParam Long gid, @RequestParam String gtoken) {

        return new R<>(ehService.getGalleryDetailByGid(gid, gtoken));
    }

    @Operation(summary = "gallery 基本信息列表", description = "获取保存在数据库的 gallery 基本信息列表")
    @GetMapping(value = "/basic/list/{pageIndex}")
    @LogAnnotation
    public R<ListResp<GalleryBasicInfo>> getGalleryBasic(@PathVariable Integer pageIndex) {

        return new R<>(ehService.getGalleryList(pageIndex));
    }

    @Operation(summary = "gallery 基本信息列表", description = "通过高级搜索获得 gallery 基本信息列表")
    @PostMapping(value = "/basic/search/{pageIndex}")
    @LogAnnotation
    public R<ListResp<GalleryBasicInfo>> getGalleryBasic(@RequestBody GalleryQuery galleryQuery, @PathVariable Integer pageIndex) {

        return new R<>(ehService.getGalleryList(galleryQuery, pageIndex));
    }

    @Operation(summary = "下载 gallery", description = "通过 gid 与 gtoken 异步下载对应 gallery")
    @PostMapping(value = "/download")
    @LogAnnotation
    public R<String> downloadGallery(@RequestBody @Validated(EhDownload.class) EhCommonReq ehCommonReq) {
        ehService.downloadGallery(ehCommonReq.getGid(), ehCommonReq.getGtoken());
        return new R<>();
    }

    @Operation(summary = "预览 gallery", description = "通过 gid 与 gtoken 获取对应页")
    @PostMapping(value = "/preview")
    @LogAnnotation(omitRes = true)
    public R<EhViewResp> previewGallery(@RequestBody @Validated(EhPreview.class) EhCommonReq ehCommonReq) {
        EhViewResp ehViewResp = new EhViewResp()
                .setPages(ehService.downloadGalleryPages(ehCommonReq.getGid(), ehCommonReq.getGtoken(), ehCommonReq.getPageIndexes()));
        return new R<>(ehViewResp);
    }

}
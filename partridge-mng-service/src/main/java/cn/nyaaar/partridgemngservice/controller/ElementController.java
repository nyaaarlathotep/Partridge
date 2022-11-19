package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.element.CollectionDto;
import cn.nyaaar.partridgemngservice.model.element.CollectionEleDto;
import cn.nyaaar.partridgemngservice.model.element.ElementDto;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.validate.Add;
import cn.nyaaar.partridgemngservice.model.validate.Delete;
import cn.nyaaar.partridgemngservice.service.element.ElementMngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * elementController
 *
 * @author nyaaar
 * @Version $Id: ElementController.java, v 0.1 2022-22 15:03 nyaaar Exp $$
 */
@Tag(name = "element")
@RestController
@RequestMapping("/element")
@Slf4j
public class ElementController {
    // TODO getUserCollection
    // TODO crud element to collection
    // TODO get user like elements
    // TODO get user like collection
    private final ElementMngService elementMngService;

    public ElementController(ElementMngService elementMngService) {
        this.elementMngService = elementMngService;
    }

    @Operation(summary = "获取 element 基本信息", description = "通过主键 id 获取 element 基本信息")
    @GetMapping(value = "/element")
    @LogAnnotation
    public R<ElementDto> getElementById(@RequestParam Long elementId) {

        return new R<>(elementMngService.getEle(elementId));
    }

    @Operation(summary = "获取 elements 基本信息", description = "通过主键 id 获取 element 基本信息")
    @GetMapping(value = "/elements")
    @LogAnnotation
    public R<List<ElementDto>> getElementById(@RequestParam List<Long> elementIds) {

        return new R<>(elementMngService.getElements(elementIds));
    }

    @Operation(summary = "分享 element", description = "通过主键id 分享 element")
    @GetMapping(value = "/share")
    @LogAnnotation
    public R<String> share(@RequestParam Long elementId) {
        elementMngService.share(elementId);
        return new R<>();
    }

    @Operation(summary = "公开 element", description = "通过主键id 公开 element")
    @GetMapping(value = "/publish")
    @LogAnnotation
    public R<String> publish(@RequestParam Long elementId) {
        elementMngService.publish(elementId);
        return new R<>();
    }

    @Operation(summary = "删除 element", description = "删除文件及其关联分片")
    @PostMapping(value = "/delete")
    @LogAnnotation
    public R<String> delete(@RequestBody Long eleId) {
        elementMngService.delete(eleId);
        return new R<>();
    }

    @Operation(summary = "喜爱对应的元素")
    @GetMapping(value = "/like")
    @LogAnnotation
    public R<String> like(@RequestParam Long eleId) {
        // TODO sort by like
        elementMngService.like(eleId);
        return new R<>();
    }

    @Operation(summary = "取消喜爱对应的元素")
    @GetMapping(value = "/unlike")
    @LogAnnotation
    public R<String> unlike(@RequestParam Long eleId) {
        elementMngService.unlike(eleId);
        return new R<>();
    }

    @Operation(summary = "新增合集", description = "新增合集，返回新增集合的 id")
    @PostMapping(value = "/collection/add")
    @LogAnnotation
    public R<Integer> addCollection(@RequestBody @Validated(Add.class) CollectionDto collectionDto) {

        return new R<>(elementMngService.addCollection(collectionDto));
    }

    @Operation(summary = "分享合集", description = "分享合集，分享集合中的所有元素")
    @PostMapping(value = "/collection/share")
    @LogAnnotation
    public R<String> shareCollection(@RequestBody Integer collectionId) {
        elementMngService.shareCollection(collectionId);
        return new R<>();
    }

    @Operation(summary = "获取用户对应集合")
    @GetMapping(value = "/collection/get/{pageIndex}")
    @LogAnnotation
    public R<ListResp<CollectionDto>> getCollection(@RequestParam String userName, @PathVariable Integer pageIndex) {

        return new R<>(elementMngService.getCollections(userName, pageIndex));
    }

    @Operation(summary = "合集新增元素", description = "新增元素至已分享集合会自动分享元素")
    @PostMapping(value = "/collection/add/element")
    @LogAnnotation
    public R<String> collectionAddElement(@RequestBody CollectionEleDto collectionEleDto) {
        elementMngService.collectionAddElement(collectionEleDto);
        return new R<>();
    }

    @Operation(summary = "合集删除元素")
    @PostMapping(value = "/collection/delete/element")
    @LogAnnotation
    public R<String> collectionDeleteElement(@RequestBody CollectionEleDto collectionEleDto) {
        elementMngService.collectionDeleteElement(collectionEleDto);
        return new R<>();
    }

    @Operation(summary = "删除合集")
    @PostMapping(value = "/collection/delete")
    @LogAnnotation
    public R<String> deleteCollection(@RequestBody @Validated(Delete.class) CollectionDto collectionDto) {
        elementMngService.deleteCollection(collectionDto);
        return new R<>();
    }

    @Operation(summary = "获取未上传完成的 elements 的 checkResp", description = "返回 check 的结果列表")
    @GetMapping(value = "/uploading")
    @LogAnnotation
    public R<List<CheckResp>> getUploadingElements() {

        return new R<>(elementMngService.getUploadingElements());
    }

}
package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.ElementDto;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.element.ElementMngService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    // TODO abstract upload element file...

    private final ElementService elementService;
    private final ElementMngService elementMngService;

    public ElementController(ElementService elementService,
                             ElementMngService elementMngService) {
        this.elementService = elementService;
        this.elementMngService = elementMngService;
    }

    @Operation(summary = "element基本信息", description = "通过主键id获取element基本信息")
    @GetMapping(value = "/element")
    @LogAnnotation
    public R<ElementDto> getElementById(@RequestParam Long elementId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, elementId));
        BusinessExceptionEnum.ELEMENT_NOT_FOUND.assertNotNull(element);
        ElementDto elementDto = new ElementDto()
                .setId(elementId)
                .setType(element.getType());
        return new R<>(elementDto);
    }

    @Operation(summary = "分享 element", description = "通过主键id 分享 element")
    @GetMapping(value = "/share")
    @LogAnnotation
    public R<String> shareElement(@RequestParam Integer elementId) {
        elementMngService.share(elementId);
        return new R<>();
    }

    @Operation(summary = "删除 element", description = "删除文件及其关联分片")
    @PostMapping(value = "/delete")
    @LogAnnotation
    public R<String> delete(@RequestBody Integer eleId) {
        elementMngService.delete(eleId);
        return new R<>();
    }
}
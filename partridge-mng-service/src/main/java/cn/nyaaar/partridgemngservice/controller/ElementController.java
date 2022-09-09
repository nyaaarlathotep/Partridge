package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.ElementDto;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.service.ElementService;
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
@Tag(name = "element信息")
@RestController
@RequestMapping("/element")
@Slf4j
public class ElementController {
    // TODO more flag and mng, such as shared, uploader...

    private final ElementService elementService;

    public ElementController(ElementService elementService) {
        this.elementService = elementService;
    }

    @Operation(summary = "element基本信息", description = "通过主键id获取element基本信息")
    @GetMapping(value = "/element")
    public R<ElementDto> getElementById(@RequestParam Integer elementId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, elementId));
        BusinessExceptionEnum.ELEMENT_NOT_FOUND.assertNotNull(element);
        ElementDto elementDto = new ElementDto()
                .setId(elementId)
                .setType(element.getType());
        return new R<>(elementDto);
    }

}
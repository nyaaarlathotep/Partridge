package cn.nyaaar.partridgemngservice.service.element.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.element.ElementMngService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yuegenhua
 * @Version $Id: ElementMngServiceImpl.java, v 0.1 2022-30 17:32 yuegenhua Exp $$
 */
@Service
@Slf4j
public class ElementMngServiceImpl implements ElementMngService {

    private final ElementService elementService;

    public ElementMngServiceImpl(ElementService elementService) {
        this.elementService = elementService;
    }

    @Override
    public void share(Long elementId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, elementId));
        BusinessExceptionEnum.ELEMENT_NOT_FOUND.assertNotNull(element);

        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getSharedFlag, PrConstant.YES)
                .eq(Element::getId, elementId));

    }
}
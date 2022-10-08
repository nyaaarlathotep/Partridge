package cn.nyaaar.partridgemngservice.service.element.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.service.EleFileService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.element.ElementMngService;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author yuegenhua
 * @Version $Id: ElementMngServiceImpl.java, v 0.1 2022-30 17:32 yuegenhua Exp $$
 */
@Service
@Slf4j
public class ElementMngServiceImpl implements ElementMngService {

    private final ElementService elementService;
    private final AppUserService appUserService;
    private final EleFileService eleFileService;

    public ElementMngServiceImpl(ElementService elementService, AppUserService appUserService, EleFileService eleFileService) {
        this.elementService = elementService;
        this.appUserService = appUserService;
        this.eleFileService = eleFileService;
    }

    @Override
    public void share(Long elementId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, elementId));
        BusinessExceptionEnum.ELEMENT_NOT_FOUND.assertNotNull(element);

        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getSharedFlag, PrConstant.YES)
                .eq(Element::getId, elementId));
    }


    @Override
    public void delete(Long eleId) {
        Element element = elementService.getById(eleId);
        BusinessExceptionEnum.ELEMENT_FILE_NOT_FOUND.assertNotNull(element);
        checkDeletePermission(eleId, element);
        try {
            String dir = FileUtil.getFileDir(element.getFileDir());
            File dirFile = new File(dir);
            log.info("[{}] 删除开始", dir);
            Integer deleteNum = 0;
            FileUtil.deleteDir(dirFile, deleteNum);
            log.info("[{}] 删除成功，共删除文件数量：{}", dir, deleteNum);
        } catch (IOException e) {
            log.error("file delete error, ", e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();
        }
        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getAvailableFlag, PrConstant.INVALIDATED)
                .eq(Element::getId, eleId));
    }

    private void checkDeletePermission(Long eleId, Element element) {
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkWritePermission(eleId));
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(Objects.equals(PrConstant.YES, element.getFreedFlag()));
    }

    @Override
    public void like(Long elementId) {

    }

    @Override
    public void publishElement(Long eleId) {
        Element element = elementService.getById(eleId);
        BusinessExceptionEnum.ELEMENT_FILE_NOT_FOUND.assertNotNull(element);
//        List<EleFile>

    }

    private void freeQuota(Long elementBytes) {
        appUserService.freeUserSpaceLimit(ThreadLocalUtil.getCurrentUser(), elementBytes);
    }

    @Override
    public boolean checkReadPermission(Long elementId) {
        if ("root".equals(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        Element element = elementService.getById(elementId);
        return Objects.equals(PrConstant.YES, element.getSharedFlag());
    }

    @Override
    public boolean checkWritePermission(Long elementId) {
        if ("root".equals(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        Element element = elementService.getById(elementId);
        return Objects.equals(ThreadLocalUtil.getCurrentUser(), element.getUploader());
    }
}
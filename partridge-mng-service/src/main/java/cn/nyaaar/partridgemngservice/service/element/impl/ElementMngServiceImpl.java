package cn.nyaaar.partridgemngservice.service.element.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.entity.EleFile;
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
    public void share(Integer elementId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, elementId));
        BusinessExceptionEnum.ELEMENT_NOT_FOUND.assertNotNull(element);

        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getSharedFlag, PrConstant.YES)
                .eq(Element::getId, elementId));
    }


    @Override
    public void delete(Integer eleId) {
        EleFile eleFile = eleFileService.findById(eleId);
        BusinessExceptionEnum.ELEMENT_FILE_NOT_FOUND.assertNotNull(eleFile);
        Element element = elementService.getById(eleFile.getEleId());
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkDeletePermission(element));
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(Objects.equals(PrConstant.YES, element.getFreedFlag()));
        try {
            String dir = FileUtil.getFileDir(eleFile.getPath());
            File dirFile = new File(dir);
            log.info("[{}] 删除开始", dir);
            Integer deleteNum = 0;
            FileUtil.deleteDir(dirFile, deleteNum);
            log.info("[{}] 删除成功，共删除文件数量：{}", dir, deleteNum);
        } catch (IOException e) {
            log.error("file delete error, ", e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();
        }
        eleFileService.update(Wrappers.lambdaUpdate(EleFile.class)
                .set(EleFile::getAvailableFlag, PrConstant.INVALIDATED)
                .eq(EleFile::getId, eleFile.getId()));
    }

    private static boolean checkDeletePermission(Element element) {
        if ("root".equals(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        return Objects.equals(ThreadLocalUtil.getCurrentUser(), element.getUploader());
    }

    @Override
    public void like(Integer elementId) {

    }

    @Override
    public void publishElement(Integer elementId) {

    }

    private void freeQuota(Long elementBytes) {
        appUserService.freeUserSpaceLimit(ThreadLocalUtil.getCurrentUser(), elementBytes);
    }
}
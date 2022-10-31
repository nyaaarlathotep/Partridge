package cn.nyaaar.partridgemngservice.service.element.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.enums.CompleteFlagEnum;
import cn.nyaaar.partridgemngservice.common.enums.EleOrgReTypeEnum;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.ElementDto;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.service.*;
import cn.nyaaar.partridgemngservice.service.element.ElementMngService;
import cn.nyaaar.partridgemngservice.service.transmit.UploadService;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.StringUtils;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private final FileUploadInfoService fileUploadInfoService;
    private final UploadService uploadService;
    private final EleOrgReService eleOrgReService;
    private final OrganizationService organizationService;
    private final ActorService actorService;
    private final EleActorReService eleActorReService;
    private final TagInfoService tagInfoService;
    private final EleTagReService eleTagReService;
    private final UserEleLikeService userEleLikeService;

    public ElementMngServiceImpl(ElementService elementService,
                                 AppUserService appUserService,
                                 EleFileService eleFileService,
                                 FileUploadInfoService fileUploadInfoService,
                                 UploadService uploadService,
                                 EleOrgReService eleOrgReService,
                                 OrganizationService organizationService,
                                 ActorService actorService,
                                 EleActorReService eleActorReService,
                                 TagInfoService tagInfoService,
                                 EleTagReService eleTagReService,
                                 UserEleLikeService userEleLikeService) {
        this.elementService = elementService;
        this.appUserService = appUserService;
        this.eleFileService = eleFileService;
        this.fileUploadInfoService = fileUploadInfoService;
        this.uploadService = uploadService;
        this.eleOrgReService = eleOrgReService;
        this.organizationService = organizationService;
        this.actorService = actorService;
        this.eleActorReService = eleActorReService;
        this.tagInfoService = tagInfoService;
        this.eleTagReService = eleTagReService;
        this.userEleLikeService = userEleLikeService;
    }

    @Override
    public void share(Long eleId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, eleId));
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(element, "元素");
        checkEleFilesCompleted(eleId);
        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getSharedFlag, PrConstant.YES)
                .eq(Element::getId, eleId));
    }

    @Override
    public ElementDto getEle(Long eleId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, eleId));
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(element, "元素");
        List<UserEleLike> likes = userEleLikeService.list(Wrappers.lambdaQuery(UserEleLike.class)
                .eq(UserEleLike::getEleId, eleId)
                .eq(UserEleLike::getAvailableFlag, PrConstant.VALIDATED));
        return new ElementDto()
                .setId(eleId)
                .setType(element.getType())
                .setLikes(likes.size());
    }

    @Override
    public void delete(Long eleId) {
        Element element = getElement(eleId);
        checkDeletePermission(eleId, element);
        try {
            String dir = FileUtil.getFileDir(element.getFileDir());
            if (StringUtils.isNotEmpty(dir)) {
                File dirFile = new File(dir);
                log.info("[{}],[{}] 删除开始", eleId, dir);
                Integer deleteNum = 0;
                FileUtil.deleteDir(dirFile, deleteNum);
                log.info("[{}],[{}] 删除成功，共删除文件数量：{}", eleId, dir, deleteNum);
            }

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
        BusinessExceptionEnum.PERMISSION_DENY.assertIsFalse(Objects.equals(PrConstant.YES, element.getPublishedFlag()));
    }

    @Override
    public void like(Long eleId) {
        getElement(eleId);
        UserEleLike userEleLike = userEleLikeService.getOne(Wrappers.lambdaQuery(UserEleLike.class)
                .eq(UserEleLike::getEleId, eleId)
                .eq(UserEleLike::getUserName, ThreadLocalUtil.getCurrentUser()));
        if (userEleLike == null) {
            userEleLike = new UserEleLike()
                    .setEleId(eleId)
                    .setUserName(ThreadLocalUtil.getCurrentUser())
                    .setAvailableFlag(PrConstant.VALIDATED);
            userEleLikeService.save(userEleLike);
        } else {
            userEleLike.setAvailableFlag(PrConstant.VALIDATED);
            userEleLikeService.updateById(userEleLike);
        }
    }

    @Override
    public void unlike(Long eleId) {
        getElement(eleId);
        UserEleLike userEleLike = userEleLikeService.getOne(Wrappers.lambdaQuery(UserEleLike.class)
                .eq(UserEleLike::getEleId, eleId)
                .eq(UserEleLike::getUserName, ThreadLocalUtil.getCurrentUser()));
        if (userEleLike != null) {
            userEleLike.setAvailableFlag(PrConstant.INVALIDATED);
            userEleLikeService.updateById(userEleLike);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long eleId) {
        Element element = getElement(eleId);
        checkWritePermission(element.getId());
        checkEleFilesCompleted(eleId);
        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getPublishedFlag, PrConstant.YES)
                .eq(Element::getId, eleId));
        appUserService.freeUserSpaceLimit(ThreadLocalUtil.getCurrentUser(), element.getFileSize());
    }

    @Override
    public List<CheckResp> getUploadingElements() {
        String userName = ThreadLocalUtil.getCurrentUser();
        return elementService
                .list(Wrappers.lambdaQuery(Element.class)
                        .eq(Element::getUploader, userName)
                        .eq(Element::getAvailableFlag, PrConstant.VALIDATED))
                .stream()
                .map(element -> eleFileService.getOne(Wrappers.lambdaQuery(EleFile.class)
                        .eq(EleFile::getEleId, element.getId())
                        .eq(EleFile::getAvailableFlag, PrConstant.VALIDATED))).filter(Objects::nonNull)
                .map(eleFile -> fileUploadInfoService.getOne(Wrappers.lambdaQuery(FileUploadInfo.class)
                        .eq(FileUploadInfo::getEleFileId, eleFile.getId())
                        .eq(FileUploadInfo::getUploadFlag, PrConstant.NO))).filter(Objects::nonNull)
                .map(fileUploadInfo -> uploadService.check(fileUploadInfo.getEleFileId())).filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Optional<Organization> getEleOrgan(Long eleId, EleOrgReTypeEnum eleOrgReTypeEnum) {
        List<EleOrgRe> eleOrgRes = eleOrgReService.list(new LambdaQueryWrapper<EleOrgRe>().
                eq(EleOrgRe::getEleId, eleId));
        List<Organization> organs = organizationService.list(Wrappers.lambdaQuery(Organization.class)
                .in(Organization::getId,
                        eleOrgRes.stream().map(EleOrgRe::getOrgId).toList()));
        return organs.stream().filter(organization -> eleOrgReTypeEnum.getRe().equals(organization.getType())).findFirst();
    }

    @Override
    public List<Actor> getEleActors(Long eleId) {
        List<EleActorRe> eleActorRes = eleActorReService.list(new LambdaQueryWrapper<EleActorRe>().
                eq(EleActorRe::getEleId, eleId));
        return actorService.list(new LambdaQueryWrapper<Actor>()
                .in(Actor::getId, eleActorRes.stream().map(EleActorRe::getActorId).toList()));
    }

    @Override
    public List<TagInfo> getTagInfos(long eleId) {
        List<EleTagRe> eleTagRes = eleTagReService.list(
                new LambdaQueryWrapper<EleTagRe>().eq(EleTagRe::getEleId, eleId));
        return tagInfoService.list(new LambdaQueryWrapper<TagInfo>().
                in(TagInfo::getId, eleTagRes.stream().map(EleTagRe::getTagId).toList()));
    }

    private void checkEleFilesCompleted(Long eleId) {
        List<EleFile> eleFiles = eleFileService.list(Wrappers.lambdaQuery(EleFile.class)
                .eq(EleFile::getEleId, eleId));
        for (EleFile eleFile : eleFiles) {
            if (!CompleteFlagEnum.completed(eleFile.getCompletedFlag())) {
                BusinessExceptionEnum.COMMON_BUSINESS_ERROR.assertFail("元素未完成，无法操作");
            }
        }
    }

    @Override
    public boolean checkReadPermission(Long eleId) {
        if (appUserService.isRoot(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        Element element = elementService.getById(eleId);
        return Objects.equals(PrConstant.YES, element.getSharedFlag());
    }

    @Override
    public boolean checkWritePermission(Long eleId) {
        if (appUserService.isRoot(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        Element element = elementService.getById(eleId);
        return Objects.equals(ThreadLocalUtil.getCurrentUser(), element.getUploader());
    }

    private Element getElement(Long eleId) {
        Element element = elementService.getById(eleId);
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(element, "元素");
        return element;
    }
}
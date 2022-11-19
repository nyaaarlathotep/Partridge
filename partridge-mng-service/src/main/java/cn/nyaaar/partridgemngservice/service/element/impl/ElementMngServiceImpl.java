package cn.nyaaar.partridgemngservice.service.element.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.common.enums.CompleteFlagEnum;
import cn.nyaaar.partridgemngservice.common.enums.EleOrgReTypeEnum;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.element.CollectionDto;
import cn.nyaaar.partridgemngservice.model.element.CollectionEleDto;
import cn.nyaaar.partridgemngservice.model.element.ElementDto;
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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author yuegenhua
 * @Version $Id: ElementMngServiceImpl.java, v 0.1 2022-30 17:32 yuegenhua Exp $$
 */
@Service
@Slf4j
public class ElementMngServiceImpl implements ElementMngService {
    // TODO element thumbnail

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
    private final PrCollectionService prcs;
    private final UserCollectionLikeService ucls;
    private final CollectionEleReService collectionEleReService;

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
                                 UserEleLikeService userEleLikeService,
                                 PrCollectionService prcs,
                                 UserCollectionLikeService ucls,
                                 CollectionEleReService collectionEleReService) {
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
        this.prcs = prcs;
        this.ucls = ucls;
        this.collectionEleReService = collectionEleReService;
    }

    @Override
    public void share(Long eleId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, eleId));
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(element, "元素");
        if (Objects.equals(element.getSharedFlag(), PrConstant.YES)) {
            return;
        }
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkWritePermission(element.getId()));
        BusinessExceptionEnum.ELEMENT_UNCOMPLETED.assertIsTrue(checkEleFilesCompleted(eleId));
        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getSharedFlag, PrConstant.YES)
                .eq(Element::getId, eleId));
        log.info("[{}] element shared", eleId);
    }

    @Override
    public ElementDto getEle(Long eleId) {
        Element element = elementService.getOne(new LambdaQueryWrapper<Element>().eq(Element::getId, eleId));
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(element, "元素");
        return getElementDto(element);
    }

    @Override
    public List<ElementDto> getElements(List<Long> elementIds) {
        if (elementIds.isEmpty()) {
            return Collections.emptyList();
        }
        return elementService.list(Wrappers.lambdaQuery(Element.class)
                        .in(Element::getId, elementIds)).stream()
                .map(this::getElementDto)
                .sorted(Comparator.comparing(ElementDto::getId))
                .toList();
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
        } else if (!Objects.equals(userEleLike.getAvailableFlag(), PrConstant.VALIDATED)) {
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
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkWritePermission(element.getId()));
        BusinessExceptionEnum.ELEMENT_UNCOMPLETED.assertIsTrue(checkEleFilesCompleted(eleId));
        BusinessExceptionEnum.COMMON_BUSINESS_ERROR.assertIsTrue(checkElementPublish(eleId),
                "未达到元素释放条件，需要至少 " + Settings.getElementPublishMinLike() + " 次喜爱");

        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getPublishedFlag, PrConstant.YES)
                .eq(Element::getId, eleId));
        appUserService.freeUserSpaceLimit(ThreadLocalUtil.getCurrentUser(), element.getFileSize());
    }

    @Override
    public Integer addCollection(CollectionDto collectionDto) {
        PrCollection prCollection = new PrCollection()
                .setAvailableFlag(PrConstant.VALIDATED)
                .setUserName(ThreadLocalUtil.getCurrentUser())
                .setCName(collectionDto.getName())
                .setCDesc(collectionDto.getDesc())
                .setSharedFlag(PrConstant.NO);
        prcs.save(prCollection);
        log.info("[{}] collection created", prCollection.getId());
        return prCollection.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shareCollection(Integer collectionId) {
        PrCollection prCollection = getPrCollection(collectionId);
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkWritePermission(prCollection));

        getEleIds(prCollection)
                .forEach(this::share);
        prcs.update(Wrappers.lambdaUpdate(PrCollection.class)
                .set(PrCollection::getSharedFlag, PrConstant.YES)
                .eq(PrCollection::getId, collectionId));
        log.info("[{}] collection shared", prCollection.getId());
    }


    @Override
    public void collectionAddElement(CollectionEleDto collectionEleDto) {
        PrCollection collection = getPrCollection(collectionEleDto.getCollectionId());
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkWritePermission(collection));
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkReadPermission(collectionEleDto.getEleId()));
        BusinessExceptionEnum.ELEMENT_UNCOMPLETED.assertIsTrue(checkEleFilesCompleted(collectionEleDto.getEleId()));
        if (Objects.equals(collection.getSharedFlag(), PrConstant.YES)) {
            share(collectionEleDto.getEleId());
        }

        collectionEleReService.save(new CollectionEleRe()
                .setCollectionId(collectionEleDto.getCollectionId())
                .setEleId(collectionEleDto.getEleId()));
        log.info("[{}] collection add element: [{}]", collectionEleDto.getCollectionId(), collectionEleDto.getEleId());
    }

    @Override
    public void collectionDeleteElement(CollectionEleDto collectionEleDto) {
        PrCollection collection = getPrCollection(collectionEleDto.getCollectionId());
        getElement(collectionEleDto.getEleId());

        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkWritePermission(collection));
        collectionEleReService.remove(Wrappers.lambdaQuery(CollectionEleRe.class)
                .eq(CollectionEleRe::getCollectionId, collectionEleDto.getCollectionId())
                .eq(CollectionEleRe::getEleId, collectionEleDto.getEleId()));
        log.info("[{}] collection delete element: [{}]", collectionEleDto.getCollectionId(), collectionEleDto.getEleId());
    }

    public void deleteCollection(CollectionDto collectionDto) {
        PrCollection collection = getPrCollection(collectionDto.getId());
        BusinessExceptionEnum.PERMISSION_DENY.assertIsTrue(checkWritePermission(collection));
        prcs.update(Wrappers.lambdaUpdate(PrCollection.class)
                .set(PrCollection::getAvailableFlag, PrConstant.INVALIDATED)
                .eq(PrCollection::getId, collectionDto.getId()));
        log.info("[{}] collection deleted!", collectionDto.getId());
    }

    @Override
    public ListResp<CollectionDto> getCollections(String userName, Integer pageIndex) {
        if (StringUtils.isEmpty(userName)) {
            userName = ThreadLocalUtil.getCurrentUser();
        }
        Page<PrCollection> page = new Page<>(pageIndex, 10);
        prcs.page(page, Wrappers.lambdaQuery(PrCollection.class)
                .eq(PrCollection::getUserName, userName)
                .eq(PrCollection::getAvailableFlag, PrConstant.VALIDATED)
                .eq(PrCollection::getSharedFlag, PrConstant.YES)
                .orderByDesc(PrCollection::getId));
        return new ListResp<CollectionDto>()
                .setList(getCollectionDtos(page.getRecords()))
                .setPages(page.getPages())
                .setCurrent(page.getCurrent());
    }

    @NotNull
    private List<CollectionDto> getCollectionDtos(List<PrCollection> prCollections) {
        if (prCollections.isEmpty()) {
            return Collections.emptyList();
        }
        List<UserCollectionLike> likes = ucls.list(Wrappers.lambdaQuery(UserCollectionLike.class)
                .in(UserCollectionLike::getCollectionId,
                        prCollections.stream()
                                .map(PrCollection::getId)
                                .toList()));

        return prCollections.parallelStream()
                .map(prCollection -> new CollectionDto()
                        .setId(prCollection.getId())
                        .setName(prCollection.getCName())
                        .setDesc(prCollection.getCDesc())
                        .setLikes((int) likes.stream()
                                .filter(like -> like.getCollectionId().equals(prCollection.getId()))
                                .count())
                        .setElementDtos(getCollectionEleDtos(prCollection)))
                .toList();
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
                .sorted(Comparator.comparing(CheckResp::getEleFileId))
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
        List<Integer> eleTagIds = eleTagReService.list(new LambdaQueryWrapper<EleTagRe>()
                        .eq(EleTagRe::getEleId, eleId))
                .stream()
                .map(EleTagRe::getTagId)
                .toList();
        if (eleTagIds.isEmpty()) {
            return Collections.emptyList();
        }
        return tagInfoService.list(new LambdaQueryWrapper<TagInfo>().
                in(TagInfo::getId, eleTagIds));
    }

    private boolean checkEleFilesCompleted(Long eleId) {
        List<EleFile> eleFiles = eleFileService.list(Wrappers.lambdaQuery(EleFile.class)
                .eq(EleFile::getEleId, eleId));
        for (EleFile eleFile : eleFiles) {
            if (!CompleteFlagEnum.completed(eleFile.getCompletedFlag())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkReadPermission(Long eleId) {
        Element element = getElement(eleId);
        if (appUserService.isRoot(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        return Objects.equals(PrConstant.YES, element.getSharedFlag()) ||
                Objects.equals(element.getUploader(), ThreadLocalUtil.getCurrentUser());
    }

    public boolean checkReadPermission(Element element) {
        if (appUserService.isRoot(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        return Objects.equals(PrConstant.YES, element.getSharedFlag()) ||
                Objects.equals(element.getUploader(), ThreadLocalUtil.getCurrentUser());
    }

    private boolean checkWritePermission(PrCollection prCollection) {
        return Objects.equals(prCollection.getUserName(), ThreadLocalUtil.getCurrentUser())
                || appUserService.isRoot(ThreadLocalUtil.getCurrentUser());
    }

    @Override
    public boolean checkWritePermission(Long eleId) {
        Element element = getElement(eleId);
        if (appUserService.isRoot(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        return Objects.equals(ThreadLocalUtil.getCurrentUser(), element.getUploader());
    }

    public boolean checkWritePermission(Element element) {
        if (appUserService.isRoot(ThreadLocalUtil.getCurrentUser())) {
            return true;
        }
        return Objects.equals(ThreadLocalUtil.getCurrentUser(), element.getUploader());
    }

    @NotNull
    private Element getElement(Long eleId) {
        Element element = elementService.getById(eleId);
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(element, "元素");
        return element;
    }

    @NotNull
    private PrCollection getPrCollection(Integer collectionId) {
        PrCollection prCollection = prcs.getById(collectionId);
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(prCollection, "集合");
        return prCollection;
    }

    @NotNull
    private List<Element> getCollectionElements(PrCollection prCollection) {
        List<Long> eleIds = getEleIds(prCollection);
        if (eleIds.isEmpty()) {
            return Collections.emptyList();
        }
        return elementService.list(Wrappers.lambdaQuery(Element.class)
                .in(Element::getId, eleIds));
    }

    @NotNull
    private List<Long> getEleIds(PrCollection prCollection) {
        return collectionEleReService.list(Wrappers.lambdaQuery(CollectionEleRe.class)
                        .eq(CollectionEleRe::getCollectionId, prCollection.getId())
                        .orderByDesc(CollectionEleRe::getId))
                .parallelStream()
                .map(CollectionEleRe::getEleId)
                .toList();
    }

    @NotNull
    private List<ElementDto> getCollectionEleDtos(PrCollection prCollection) {

        return getCollectionElements(prCollection).parallelStream()
                .map(this::getElementDto)
                .toList();
    }

    private ElementDto getElementDto(Element element) {
        List<UserEleLike> likes = userEleLikeService.list(Wrappers.lambdaQuery(UserEleLike.class)
                .eq(UserEleLike::getEleId, element.getId())
                .eq(UserEleLike::getAvailableFlag, PrConstant.VALIDATED));
        return new ElementDto()
                .setId(element.getId())
                .setType(element.getType())
                .setCompleted(CompleteFlagEnum.completed(element.getCompletedFlag()))
                .setAvailable(PrConstant.VALIDATED == element.getAvailableFlag())
                .setLikes(likes.size());
    }

    private boolean checkElementPublish(Long eleId) {
        List<UserEleLike> userEleLikes = userEleLikeService.list(Wrappers.lambdaQuery(UserEleLike.class)
                .eq(UserEleLike::getEleId, eleId)
                .eq(UserEleLike::getAvailableFlag, PrConstant.VALIDATED));
        return userEleLikes.size() >= Settings.getElementPublishMinLike();

    }
}
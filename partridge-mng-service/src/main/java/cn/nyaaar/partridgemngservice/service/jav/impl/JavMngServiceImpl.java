package cn.nyaaar.partridgemngservice.service.jav.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.common.enums.EleOrgReTypeEnum;
import cn.nyaaar.partridgemngservice.common.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.TagDto;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.jav.JavQuery;
import cn.nyaaar.partridgemngservice.model.jav.JavUploadReq;
import cn.nyaaar.partridgemngservice.service.*;
import cn.nyaaar.partridgemngservice.service.file.UploadService;
import cn.nyaaar.partridgemngservice.service.jav.JavMngService;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class JavMngServiceImpl implements JavMngService {

    private final JavService javService;

    private final OrganizationService organizationService;

    private final ActorService actorService;

    private final EleActorReService eleActorReService;

    private final EleOrgReService eleOrgReService;

    private final TagInfoService tagInfoService;
    private final ElementService elementService;
    private final EleFileService eleFileService;
    private final UploadService uploadService;
    private final FileUploadInfoService fileUploadInfoService;
    private final AppUserService appUserService;

    public JavMngServiceImpl(JavService javService,
                             OrganizationService organizationService,
                             ActorService actorService,
                             EleActorReService eleActorReService,
                             EleOrgReService eleOrgReService,
                             TagInfoService tagInfoService,
                             ElementService elementService,
                             EleFileService eleFileService,
                             UploadService uploadService,
                             FileUploadInfoService fileUploadInfoService,
                             AppUserService appUserService) {
        this.javService = javService;
        this.organizationService = organizationService;
        this.actorService = actorService;
        this.eleActorReService = eleActorReService;
        this.eleOrgReService = eleOrgReService;
        this.tagInfoService = tagInfoService;
        this.elementService = elementService;
        this.eleFileService = eleFileService;
        this.uploadService = uploadService;
        this.fileUploadInfoService = fileUploadInfoService;
        this.appUserService = appUserService;
    }

    @Override
    public JavBasicInfo getJavBasicInfoByCode(String code) {
        Jav jav = javService.getOne(new LambdaQueryWrapper<Jav>().eq(Jav::getCode, code));
        BusinessExceptionEnum.ELEMENT_NOT_FOUND.assertNotNull(jav);

        return getJavBasicInfo(jav);
    }

    @Override
    public ListResp<JavBasicInfo> getJavList(JavQuery javQuery, int pageIndex) {
        Page<Jav> page = new Page<>(pageIndex, 10);
        LambdaQueryWrapper<Jav> lambdaQueryWrapper = getJavLambdaQueryWrapper(javQuery);
        queryPage(javQuery, page, lambdaQueryWrapper);
        return getJInfoListResp(page);
    }

    @Override
    public ListResp<JavBasicInfo> getJavList(int pageIndex) {
        Page<Jav> page = new Page<>(pageIndex, 10);
        javService.page(page, Wrappers.lambdaQuery(Jav.class).orderByDesc(Jav::getEleId));
        return getJInfoListResp(page);
    }

    @Override
    public CheckResp uploadJav(JavUploadReq javUploadReq) {
        checkQuota();
        Element element = new Element()
                .setType(SourceEnum.Jav.getCode())
                .setUploader(ThreadLocalUtil.getCurrentUser())
                .setFileSize(0L)
                .setAvailableFlag(PrConstant.VALIDATED)
                .setSharedFlag(PrConstant.NO);
        elementService.save(element);
        // TODO rpc call pelican to crawl javInfo
        EleFile eleFile = new EleFile()
                .setEleId(element.getId())
                .setType(FileTypeEnum.getTypeBySuffix(javUploadReq.getFileName()).getSuffix())
                .setAvailableFlag(PrConstant.VALIDATED)
                .setName(FileUtil.legalizeFileName(javUploadReq.getFileName()));
        eleFileService.save(eleFile);
        CheckResp checkResp = null;
        try {
            checkResp = uploadService.check(javUploadReq.getFileName(), javUploadReq.getFileMd5(),
                    javUploadReq.getFileSize(), eleFile.getId(), javUploadReq.getUploaderPath());
        } catch (IOException e) {
            log.error("check error, ", e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();
        }
        return checkResp;
    }

    private void checkQuota() {
        String userName = ThreadLocalUtil.getCurrentUser();
        List<FileUploadInfo> uploadingFiles = elementService
                .list(Wrappers.lambdaQuery(Element.class)
                        .eq(Element::getUploader, userName))
                .stream()
                .map(element -> eleFileService.getOne(Wrappers.lambdaQuery(EleFile.class)
                        .eq(EleFile::getEleId, element.getId()))).filter(Objects::nonNull)
                .map(eleFile -> fileUploadInfoService.getOne(Wrappers.lambdaQuery(FileUploadInfo.class)
                        .eq(FileUploadInfo::getEleFileId, eleFile.getId())
                        .eq(FileUploadInfo::getUploadFlag, PrConstant.UPLOADING))).filter(Objects::nonNull)
                .toList();
        if (uploadingFiles.size() >= Settings.getJavUploadingMax()) {
            BusinessExceptionEnum.USER_CUSTOM.assertFail("已存在 " + uploadingFiles.size() + " 个正在上传的文件，请先上传完成。");
        }
        BusinessExceptionEnum.SPACE_INSUFFICIENT.assertIsTrue(appUserService.checkUserSpaceLimit(ThreadLocalUtil.getCurrentUser()));
    }

    @Override
    public List<CheckResp> getUploadingJavs() {
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
                        .eq(FileUploadInfo::getUploadFlag, PrConstant.UPLOADING))).filter(Objects::nonNull)
                .map(fileUploadInfo -> uploadService.check(fileUploadInfo.getEleFileId())).filter(Objects::nonNull)
                .toList();
    }

    private void queryPage(JavQuery javQuery, Page<Jav> page, LambdaQueryWrapper<Jav> lambdaQueryWrapper) {
        List<Integer> actorIds = null;
        if (Objects.nonNull(javQuery.getActors()) && !javQuery.getActors().isEmpty()) {
            actorIds = javQuery.getActors()
                    .parallelStream()
                    .map(queryActorName -> actorService.getOne
                            (Wrappers.lambdaQuery(Actor.class).eq(Actor::getName, queryActorName)))
                    .filter(Objects::nonNull)
                    .map(Actor::getId)
                    .toList();
        }
        List<Integer> organIds = null;
        if (Objects.nonNull(javQuery.getOrganizations()) && !javQuery.getOrganizations().isEmpty()) {
            organIds = javQuery.getOrganizations()
                    .parallelStream()
                    .map(queryOrganName -> organizationService.getOne
                            (Wrappers.lambdaQuery(Organization.class).eq(Organization::getName, queryOrganName)))
                    .filter(Objects::nonNull)
                    .map(Organization::getId)
                    .toList();
        }
        List<Integer> tagInfoIds = null;
        if (Objects.nonNull(javQuery.getTagDtos()) && !javQuery.getTagDtos().isEmpty()) {
            tagInfoIds = javQuery.getTagDtos()
                    .parallelStream()
                    .map(queryTagInfo -> tagInfoService.getOne(Wrappers.query(queryTagInfo.transToEntity())))
                    .filter(Objects::nonNull)
                    .map(TagInfo::getId)
                    .toList();
        }
        javService.pageWithTag(page, lambdaQueryWrapper, tagInfoIds, actorIds, organIds);
    }

    @NotNull
    private static LambdaQueryWrapper<Jav> getJavLambdaQueryWrapper(JavQuery javQuery) {
        LambdaQueryWrapper<Jav> lambdaQueryWrapper = Wrappers.lambdaQuery(Jav.class);

        if (javQuery.getCode() != null) {
            lambdaQueryWrapper.eq(Jav::getCode, javQuery.getCode());
        }
        if (javQuery.getEleId() != null) {
            lambdaQueryWrapper.eq(Jav::getEleId, javQuery.getEleId());
        }
        if (javQuery.getTitle() != null) {
            lambdaQueryWrapper.like(Jav::getTitle, javQuery.getTitle());
        }
        if (javQuery.getPublishDateStart() != null) {
            lambdaQueryWrapper.ge(Jav::getPublishDate, javQuery.getPublishDateStart());
        }
        if (javQuery.getPublishDateEnd() != null) {
            lambdaQueryWrapper.le(Jav::getPublishDate, javQuery.getPublishDateEnd());
        }
        if (javQuery.getLengthCeiling() != null) {
            lambdaQueryWrapper.le(Jav::getLength, javQuery.getLengthCeiling());
        }
        if (javQuery.getLengthFloor() != null) {
            lambdaQueryWrapper.ge(Jav::getLength, javQuery.getLengthFloor());
        }
        if (javQuery.getDirector() != null) {
            lambdaQueryWrapper.eq(Jav::getDirector, javQuery.getDirector());
        }
        if (javQuery.getSeries() != null) {
            lambdaQueryWrapper.eq(Jav::getSeries, javQuery.getSeries());
        }
        return lambdaQueryWrapper;
    }


    @NotNull
    private ListResp<JavBasicInfo> getJInfoListResp(Page<Jav> page) {
        List<JavBasicInfo> javBasicInfos = page.getRecords()
                .parallelStream()
                .map(this::getJavBasicInfo)
                .toList();

        return new ListResp<JavBasicInfo>()
                .setList(javBasicInfos)
                .setCurrent(page.getCurrent())
                .setPages(page.getPages());
    }

    @NotNull
    private JavBasicInfo getJavBasicInfo(Jav jav) {
        JavBasicInfo javBasicInfo = new JavBasicInfo();
        BeanUtil.copyProperties(jav, javBasicInfo);
        List<EleActorRe> eleActorRes = eleActorReService.list(new LambdaQueryWrapper<EleActorRe>().
                eq(EleActorRe::getEleId, jav.getEleId()));

        if (!eleActorRes.isEmpty()) {
            List<Actor> actors = actorService.list(new LambdaQueryWrapper<Actor>().in(Actor::getId,
                    eleActorRes.stream().map(EleActorRe::getActorId).toList()));
            javBasicInfo.setActors(actors.stream().map(Actor::getName).toList());
        }

        EleOrgRe eleOrgReProduce = eleOrgReService.getOne(new LambdaQueryWrapper<EleOrgRe>().
                eq(EleOrgRe::getEleId, jav.getEleId()).
                eq(EleOrgRe::getReType, EleOrgReTypeEnum.produce.getRe()));
        if (eleOrgReProduce != null) {
            Organization producer = organizationService.findById(eleOrgReProduce.getOrgId());
            javBasicInfo.setProducer(producer.getName());
        }
        EleOrgRe eleOrgRePublisher = eleOrgReService.getOne(new LambdaQueryWrapper<EleOrgRe>().
                eq(EleOrgRe::getEleId, jav.getEleId()).
                eq(EleOrgRe::getReType, EleOrgReTypeEnum.publish.getRe()));
        if (eleOrgRePublisher != null) {
            Organization producer = organizationService.findById(eleOrgRePublisher.getOrgId());
            javBasicInfo.setPublisher(producer.getName());
        }
        List<TagInfo> tagInfos = tagInfoService.getTagInfos(javBasicInfo.getEleId());
        javBasicInfo.setTags(tagInfos.stream().map(TagDto::new).toList());
        return javBasicInfo;
    }
}

package cn.nyaaar.partridgemngservice.service.video.jav.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.nyaaar.partridgemngservice.common.enums.EleOrgReTypeEnum;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.TagDto;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.jav.JavQuery;
import cn.nyaaar.partridgemngservice.model.jav.JavUploadReq;
import cn.nyaaar.partridgemngservice.service.*;
import cn.nyaaar.partridgemngservice.service.element.ElementMngService;
import cn.nyaaar.partridgemngservice.service.transmit.UploadService;
import cn.nyaaar.partridgemngservice.service.video.jav.JavMngService;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import cn.nyaaar.partridgemngservice.service.video.Video;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class JavMngServiceImpl extends Video implements JavMngService {

    private final JavService javService;
    private final OrganizationService organizationService;
    private final ActorService actorService;
    private final TagInfoService tagInfoService;
    private final ElementMngService elementMngService;

    public JavMngServiceImpl(JavService javService,
                             OrganizationService organizationService,
                             ActorService actorService,
                             TagInfoService tagInfoService,
                             ElementService elementService,
                             EleFileService eleFileService,
                             UploadService uploadService,
                             FileUploadInfoService fileUploadInfoService,
                             AppUserService appUserService,
                             ElementMngService elementMngService) {
        super(elementService, appUserService, eleFileService, fileUploadInfoService, uploadService);
        this.javService = javService;
        this.organizationService = organizationService;
        this.actorService = actorService;
        this.tagInfoService = tagInfoService;
        this.elementMngService = elementMngService;
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
    public CheckResp uploadJav(JavUploadReq javUploadReq) {
        checkQuota();
        EleFile eleFile = preUploadHandle(javUploadReq);
        // TODO rpc call pelican to crawl javInfo
        CheckResp checkResp = getCheckResp(javUploadReq, eleFile);
        postUploadHandle();
        return checkResp;
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
        lambdaQueryWrapper.orderByDesc(Jav::getEleId);
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
        javBasicInfo.setActors(elementMngService.getEleActors(jav.getEleId()).stream().map(Actor::getName).toList());
        javBasicInfo.setProducer(elementMngService.getEleOrgan(jav.getEleId(), EleOrgReTypeEnum.produce)
                .map(Organization::getName).orElse(""));
        javBasicInfo.setPublisher(elementMngService.getEleOrgan(jav.getEleId(), EleOrgReTypeEnum.publish)
                .map(Organization::getName).orElse(""));
        javBasicInfo.setTags(elementMngService.getTagInfos(jav.getEleId()).stream().map(TagDto::new).toList());
        return javBasicInfo;
    }
}

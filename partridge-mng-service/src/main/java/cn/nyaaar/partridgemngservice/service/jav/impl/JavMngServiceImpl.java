package cn.nyaaar.partridgemngservice.service.jav.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.nyaaar.partridgemngservice.common.enums.EleOrgReTypeEnum;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.TagDto;
import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.jav.JavQuery;
import cn.nyaaar.partridgemngservice.service.*;
import cn.nyaaar.partridgemngservice.service.jav.JavMngService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JavMngServiceImpl implements JavMngService {

    private final JavService javService;

    private final OrganizationService organizationService;

    private final ActorService actorService;

    private final EleActorReService eleActorReService;

    private final EleOrgReService eleOrgReService;

    private final TagInfoService tagInfoService;

    public JavMngServiceImpl(JavService javService,
                             OrganizationService organizationService,
                             ActorService actorService,
                             EleActorReService eleActorReService,
                             EleOrgReService eleOrgReService,
                             TagInfoService tagInfoService) {
        this.javService = javService;
        this.organizationService = organizationService;
        this.actorService = actorService;
        this.eleActorReService = eleActorReService;
        this.eleOrgReService = eleOrgReService;
        this.tagInfoService = tagInfoService;
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
        // TODO
//        List<Integer> actorIds=
//        javService.pageWithTag(page,)
        
        
        return getJInfoListResp(page);
    }


    @Override
    public ListResp<JavBasicInfo> getJavList(int pageIndex) {
        Page<Jav> page = new Page<>(pageIndex, 10);
        javService.page(page, Wrappers.lambdaQuery(Jav.class).orderByDesc(Jav::getEleId));
        return getJInfoListResp(page);
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

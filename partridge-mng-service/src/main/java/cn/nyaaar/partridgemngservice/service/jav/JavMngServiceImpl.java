package cn.nyaaar.partridgemngservice.service.jav;

import cn.hutool.core.bean.BeanUtil;
import cn.nyaaar.partridgemngservice.common.enums.EleOrgReTypeEnum;
import cn.nyaaar.partridgemngservice.entity.*;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.jav.ListResp;
import cn.nyaaar.partridgemngservice.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class JavMngServiceImpl implements JavMngService {

    private final JavService javService;

    private final OrganizationService organizationService;

    private final ActorService actorService;

    private final EleActorReService eleActorReService;

    private final EleOrgReService eleOrgReService;


    public JavMngServiceImpl(JavService javService,
                             OrganizationService organizationService,
                             ActorService actorService,
                             EleActorReService eleActorReService,
                             EleOrgReService eleOrgReService) {
        this.javService = javService;
        this.organizationService = organizationService;
        this.actorService = actorService;
        this.eleActorReService = eleActorReService;
        this.eleOrgReService = eleOrgReService;
    }

    @Override
    public JavBasicInfo getJavBasicInfoByCode(String code) {
        Jav jav = javService.getOne(new LambdaQueryWrapper<Jav>().eq(Jav::getCode, code));
        BusinessExceptionEnum.ELEMENT_NOT_FOUND.assertNotNull(jav);

        return getJavBasicFromJav(jav);
    }

    @Override
    public ListResp<JavBasicInfo> getJavList(String name, int pageIndex) {
        Page<Jav> page = new Page<>(pageIndex, 10);
        javService.page(page, new LambdaQueryWrapper<Jav>().like(Jav::getTitle, name).orderByDesc(Jav::getEleId));

        return getJInfoListResp(page);
    }


    @Override
    public ListResp<JavBasicInfo> getJavList(int pageIndex) {
        Page<Jav> page = new Page<>(pageIndex, 10);
        javService.page(page, new LambdaQueryWrapper<Jav>().orderByDesc(Jav::getEleId));

        return getJInfoListResp(page);
    }

    @NotNull
    private ListResp<JavBasicInfo> getJInfoListResp(Page<Jav> page) {
        List<JavBasicInfo> javBasicInfos = new ArrayList<>();
        for (Jav jav : page.getRecords()) {
            JavBasicInfo javBasicInfo = getJavBasicFromJav(jav);
            javBasicInfos.add(javBasicInfo);
        }
        ListResp<JavBasicInfo> javBasicInfoListResp = new ListResp<>();
        javBasicInfoListResp.setList(javBasicInfos);
        javBasicInfoListResp.setCurrent(page.getCurrent());
        javBasicInfoListResp.setPages(page.getPages());
        return javBasicInfoListResp;
    }

    @NotNull
    private JavBasicInfo getJavBasicFromJav(Jav jav) {
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
        return javBasicInfo;
    }
}

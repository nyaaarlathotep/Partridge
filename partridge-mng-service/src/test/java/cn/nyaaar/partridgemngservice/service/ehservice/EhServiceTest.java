package cn.nyaaar.partridgemngservice.service.ehservice;

import cn.hutool.core.thread.ThreadUtil;
import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.service.EhentaiGalleryService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yuegenhua
 * @Version $Id: EhService.java, v 0.1 2022-01 17:14 yuegenhua Exp $$
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = PartridgeMngServiceApplication.class)
public class EhServiceTest {

    @Autowired
    EhService ehservice;

    @Autowired
    EhentaiGalleryService ehentaiGalleryService;
    @Autowired
    ElementService elementService;


    @Autowired
    private EhService ehService;

    @Test
    public void mybatisPlusTest() {
        Element element = new Element();
        element.setType(SourceEnum.Ehentai.getCode());
        elementService.add(element);
        log.info(Objects.toString(element));
    }

    @Test
    public void downloadGalleryTest() {
        ehService.downloadGallery(2312700, "9813f4654d");
        ThreadUtil.sleep(30, TimeUnit.SECONDS);
    }

    @Test
    public void getGalleryPageTest() {
        log.info(ehService.getGalleryPage(2313044, 37));
    }
}
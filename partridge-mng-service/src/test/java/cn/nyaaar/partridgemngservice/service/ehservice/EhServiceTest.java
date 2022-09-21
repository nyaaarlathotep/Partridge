package cn.nyaaar.partridgemngservice.service.ehservice;

import cn.hutool.core.thread.ThreadUtil;
import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import cn.nyaaar.partridgemngservice.model.eh.DownloadingGallery;
import cn.nyaaar.partridgemngservice.service.EhentaiGalleryService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author nyaaar
 * @Version $Id: EhService.java, v 0.1 2022-01 17:14 nyaaar Exp $$
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
        EhentaiGallery ehentaiGallery = new EhentaiGallery()
                .setTitle("test")
                .setGid(1L);
        ehentaiGalleryService.saveOrUpdate(ehentaiGallery);
        log.info(JSON.toJSONString(ehentaiGallery));
    }

    @Test
    public void downloadGalleryTest() {
        ehService.downloadGallery(2316851, "7a18d746bd");
        ThreadUtil.sleep(30, TimeUnit.SECONDS);
    }

    @Test
    public void downloadGalleryQueueTest() {
        log.info("------before start-------");
        Map<Long, DownloadingGallery> queue = ehService.getDownloadingQueue();
        log.info(JSON.toJSONString(queue));
        log.info("------before start-------");
        ehService.downloadGallery(2315232, "c4fdf93bea");

        log.info("------after start-------");
        queue = ehService.getDownloadingQueue();
        log.info(JSON.toJSONString(queue));
        log.info("------after start-------");
        ThreadUtil.sleep(30, TimeUnit.SECONDS);

        log.info("------finally-------");
        queue = ehService.getDownloadingQueue();
        log.info(JSON.toJSONString(queue));
        log.info("------finally-------");
    }

    @Test
    public void getGalleryPageTest() {
        log.info(ehService.getGalleryPage(2312700, 41));
    }
}
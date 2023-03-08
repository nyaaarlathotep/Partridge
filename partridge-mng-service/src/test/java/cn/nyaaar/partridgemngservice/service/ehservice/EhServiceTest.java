package cn.nyaaar.partridgemngservice.service.ehservice;

import cn.hutool.core.thread.ThreadUtil;
import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import cn.nyaaar.partridgemngservice.entity.Element;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
        Collection<DownloadingGallery> queue = ehService.getDownloadingQueue();
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

    @Test
    public void generateElementData() {
        for (int i = 46894; i < 1000000; i++) {
            List<Element> elements = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                Element element = new Element();
                element.setType(String.valueOf((int) (Math.random() * 10)));
                element.setFileDir("tettDir");
                element.setFileSize(((long) (Math.random() * 100000)));
                element.setCompletedFlag(((int) (Math.random() * 2)));
                element.setCreatedTime(new Date());
                element.setUploader("test" + i);
                element.setPublishedFlag(0);
                element.setAvailableFlag(0);
                element.setUpdatedTime(new Date());
                i++;
                elements.add(element);
            }
            elementService.saveBatch(elements);
        }
    }
}
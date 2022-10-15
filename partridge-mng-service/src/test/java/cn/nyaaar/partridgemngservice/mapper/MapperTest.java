package cn.nyaaar.partridgemngservice.mapper;

import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.EhentaiGallery;
import cn.nyaaar.partridgemngservice.entity.EleTagRe;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.entity.Jav;
import cn.nyaaar.partridgemngservice.service.ElementService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * @author nyaaar
 * @Version $Id: MapperTest.java, v 0.1 2022-15 8:57 nyaaar Exp $$
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = PartridgeMngServiceApplication.class)
public class MapperTest {

    @Autowired
    private EhentaiGalleryMapper ehentaiGalleryMapper;

    @Autowired
    private JavMapper javMapper;

    @Autowired
    private ElementService elementService;
    @Autowired
    private EleTagReMapper eleTagReMapper;

    @Test
    public void EhentaiGalleryMapperTest() {
        Page<EhentaiGallery> page = new Page<>(6, 10);

        ehentaiGalleryMapper.pageWithTag(page, Wrappers.emptyWrapper(), List.of(1679));
        log.info("total:{}", JSON.toJSONString(page.getTotal()));
        log.info(JSON.toJSONString(page.getRecords()));

        page = new Page<>(6, 10);
        ehentaiGalleryMapper.pageWithTag(page, Wrappers.emptyWrapper(), null);
        log.info("total:{}", JSON.toJSONString(page.getTotal()));
        log.info(JSON.toJSONString(page.getRecords()));
    }

    @Test
    public void produceTestData() {

        for (int i = 0; i < 100; i++) {
            Element element = new Element()
                    .setType(SourceEnum.Jav.getCode());
            elementService.save(element);
            Jav jav = new Jav()
                    .setEleId(element.getId())
                    .setCode("test" + i)
                    .setTitle("test" + i);
            javMapper.insert(jav);
            EleTagRe eleTagRe;
            if (i % 2 == 1) {
                eleTagRe = new EleTagRe()
                        .setEleId(element.getId())
                        .setTagId(1679);
            } else {
                eleTagRe = new EleTagRe()
                        .setEleId(element.getId())
                        .setTagId(1678);
            }
            eleTagReMapper.insert(eleTagRe);
        }
    }

    @Test
    public void javMapperTest() {
        Page<Jav> page = new Page<>(1, 10);
        javMapper.pageWithTag(page, Wrappers.emptyWrapper(), null, null, null);
        log.info("total:{}", JSON.toJSONString(page.getTotal()));
        log.info(JSON.toJSONString(page.getRecords()));
        page = new Page<>(1, 10);
        javMapper.pageWithTag(page, Wrappers.emptyWrapper(), List.of(1678, 1679), null, null);
        log.info("total:{}", JSON.toJSONString(page.getTotal()));
        log.info(JSON.toJSONString(page.getRecords()));
        page = new Page<>(1, 10);
        javMapper.pageWithTag(page, Wrappers.emptyWrapper(), List.of(1678), null, null);
        log.info("total:{}", JSON.toJSONString(page.getTotal()));
        log.info(JSON.toJSONString(page.getRecords()));
        page = new Page<>(1, 10);
        javMapper.pageWithTag(page, Wrappers.emptyWrapper(), null,List.of(174), null);
        log.info("total:{}", JSON.toJSONString(page.getTotal()));
        log.info(JSON.toJSONString(page.getRecords()));
        page = new Page<>(1, 10);
        javMapper.pageWithTag(page, Wrappers.emptyWrapper(), null,List.of(174,175), null);
        log.info("total:{}", JSON.toJSONString(page.getTotal()));
        log.info(JSON.toJSONString(page.getRecords()));
        page = new Page<>(1, 10);
        javMapper.pageWithTag(page, Wrappers.emptyWrapper(), List.of(1678),List.of(174,175), null);
        log.info("total:{}", JSON.toJSONString(page.getTotal()));
        log.info(JSON.toJSONString(page.getRecords()));
    }
}
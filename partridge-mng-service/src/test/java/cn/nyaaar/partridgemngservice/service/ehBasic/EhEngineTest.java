package cn.nyaaar.partridgemngservice.service.ehBasic;

import cn.hutool.core.date.TimeInterval;
import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.constants.EhUrl;
import cn.nyaaar.partridgemngservice.model.eh.GalleryDetail;
import cn.nyaaar.partridgemngservice.service.ehService.ehBasic.EhEngine;
import cn.nyaaar.partridgemngservice.util.parser.GalleryListParser;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

/**
 * @author yuegenhua
 * @Version $Id: EhEngineTest.java, v 0.1 2022-28 16:09 yuegenhua Exp $$
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = PartridgeMngServiceApplication.class)
public class EhEngineTest {
    @Autowired
    private EhEngine ehEngine;

    @Test
    public void signInTest() {
        try {
            String user = ehEngine.signIn("nyaaar", "MWuGvbTF3sEajABsaYb7");
            log.info(user);
        } catch (Throwable e) {
            log.error(e.toString());
        }
    }

    //{
    //  "category": 256,
    //  "disowned": false,
    //  "favoriteSlot": -2,
    //  "gid": 2312700,
    //  "pages": 48,
    //  "posted": "2022-08-29 16:41",
    //  "rated": false,
    //  "rating": 4.5,
    //  "simpleLanguage": "ZH",
    //  "spanGroupIndex": 0,
    //  "spanIndex": 0,
    //  "spanSize": 0,
    //  "thumb": "https://ehgt.org/t/6a/af/6aaf362eb66f00940efba2a79b5a676556f1141c-1280725-1981-2833-jpg_250.jpg",
    //  "thumbHeight": 358,
    //  "thumbWidth": 250,
    //  "title": "[Sumire Batake (Sumire)] OUR DIARY (Uma Musume Pretty Derby) [Chinese] [提灯喵汉化组] [Digital] [Incomplete]",
    //  "token": "9813f4654d",
    //  "uploader": "ann0925"
    //}
    @Test
    public void getGalleryTest() {
        try {
            GalleryListParser.Result result = ehEngine.getGalleryList("https://e-hentai.org/?f_cats=767");
            log.info(JSON.toJSONString(result));
        } catch (Throwable e) {
            log.error(e.toString());
        }
    }

    @Test
    public void getGalleryDetailTest() {
        try {
            GalleryDetail galleryDetail = ehEngine.getGalleryDetail(2312700, "9813f4654d");
            log.info(JSON.toJSONString(galleryDetail));
        } catch (Throwable e) {
            log.error(e.toString());
        }
    }

    @Test
    public void getGalleryTokenTest() {
        log.info(
                ehEngine.getGalleryToken(2313044, "8b41332140", 1)
        );
    }

    @Test
    public void getGalleryPageTest() {
        String pageUrl = getPageUrl(2313044, 1, "8b41332140", null, null);
        log.info("pageUrl:{}", pageUrl);
        log.info(
                ehEngine.getGalleryPage(pageUrl, 2313044, "849f2a02ea").toString()
        );
    }

    @Test
    public void getGalleryPageApiTest() {
        log.info(
                ehEngine.getGalleryPageApi(2313044, 1, "8b41332140", "1xw4m8s9w7d", null).toString()
        );
    }

    @Test
    public void getPTokensTest() {
        log.info(JSON.toJSONString(ehEngine.getPTokens(2313044, "849f2a02ea")));
    }


    @Test
    public void getPTokensCacheTest() {
        log.info("第一次调用：：：");
        TimeInterval timeInterval = new TimeInterval();
        log.info(JSON.toJSONString(ehEngine.getPTokens(2313044, "849f2a02ea")));
        log.info("第一次调用：：： 花费时间 {}ms ", timeInterval.intervalRestart());
        log.info("第二次调用：：：");
        log.info(JSON.toJSONString(ehEngine.getPTokens(2313044, "849f2a02ea")));
        log.info("第二次调用：：： 花费时间 {}ms ", timeInterval.interval());
    }

    private String getPageUrl(long gid, int index, String pToken,
                              String oldPageUrl, String skipHathKey) {
        String pageUrl;
        pageUrl = Objects.requireNonNullElseGet(oldPageUrl, () -> EhUrl.getPageUrl(gid, index, pToken));
        // Add skipHathKey
        if (skipHathKey != null) {
            if (pageUrl.contains("?")) {
                pageUrl += "&nl=" + skipHathKey;
            } else {
                pageUrl += "?nl=" + skipHathKey;
            }
        }
        return pageUrl;
    }
}
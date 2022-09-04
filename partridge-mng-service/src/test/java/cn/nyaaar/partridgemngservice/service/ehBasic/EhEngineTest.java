package cn.nyaaar.partridgemngservice.service.ehBasic;

import cn.hutool.core.date.TimeInterval;
import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.common.constants.EhUrl;
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

import java.util.List;
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
            log.info(JSON.toJSONString("------------tags-------------"));
            log.info(JSON.toJSONString(galleryDetail.getTags()));
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

    // ["6fd27a8076","a88203ff85","f35631d8f8","ae1d010d2a","a7d73a46af","9d534dae1a","34a8724739","77e9760cb3","d5681a2072","20a475cd31","e7d341827f","6a85b6ae62","02a07dc328","bc4cdd223e","8003d1d2bd","a61845f4bf","54fc2ff829","965c2458b8","6157585b65","02dfe64e0c","57471faf6f","c92649f0f1","4b8d85fee3","98b154b770","caeb035ff4","66c5833ea9","1204d85b78","02e2ed9704","c292adb5a6","7c99554751","c1a63b12f2","63b8eeb8c9","c1de4cfbd5","f86b27596a","97349459a9","9f6a48fd49","c88f09fe63","9b2fce601c","9df8c70eed","1cbb0b536c"]
    @Test
    public void getPTokensTest() {
        List<String> pToken=ehEngine.getPTokens(2316851, "7a18d746bd");
        log.info(pToken.size()+"");
        log.info(JSON.toJSONString(pToken));
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
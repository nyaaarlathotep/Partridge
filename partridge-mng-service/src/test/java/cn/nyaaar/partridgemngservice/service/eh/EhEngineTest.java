package cn.nyaaar.partridgemngservice.service.eh;

import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.model.eh.GalleryDetail;
import cn.nyaaar.partridgemngservice.util.parser.GalleryListParser;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
            GalleryDetail galleryDetail= ehEngine.getGalleryDetail("https://e-hentai.org/g/2312700/9813f4654d/");
            log.info(JSON.toJSONString(galleryDetail));
        } catch (Throwable e) {
            log.error(e.toString());
        }
    }
}
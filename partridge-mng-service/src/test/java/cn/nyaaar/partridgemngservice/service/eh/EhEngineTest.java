package cn.nyaaar.partridgemngservice.service.eh;

import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.util.parser.GalleryListParser;
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

    @Test
    public void getGalleryTest() {
        try {
            GalleryListParser.Result result = ehEngine.getGalleryList("https://e-hentai.org/?f_cats=256");
            log.info(result.toString());
        } catch (Throwable e) {
            log.error(e.toString());
        }
    }
}
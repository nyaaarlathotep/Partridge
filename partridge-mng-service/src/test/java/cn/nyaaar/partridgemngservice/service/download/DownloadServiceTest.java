package cn.nyaaar.partridgemngservice.service.download;

import cn.hutool.core.thread.ThreadUtil;
import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.constants.Settings;
import cn.nyaaar.partridgemngservice.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.service.ehService.EhService;
import cn.nyaaar.partridgemngservice.service.file.FileSaveService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author yuegenhua
 * @Version $Id: DownloadServiceTest.java, v 0.1 2022-31 17:44 yuegenhua Exp $$
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = PartridgeMngServiceApplication.class)
public class DownloadServiceTest {

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private EhService ehService;

    @Autowired
    private FileSaveService fileSaveService;

    @Test
    public void downloadTest() {
        downloadService.downloadUrlToDest("https://iymlvib.fawvwgxlrdfx.hath.network/" +
                "h/8b41332140786b1c6f41d48dc2ba43cbab6c297c-49191-1100-1500-jpg/keystamp=1661940000-369c7cedf3;" +
                "fileindex=113277182;xres=org/1211145_80751202_p1.jpg", Settings.getDownloadRootPath() + "2313044\\", "2");

        ThreadUtil.sleep(3, TimeUnit.SECONDS);
    }

    @Test
    public void downloadGalleryTest() {
        ehService.downloadGallery(2313044, "849f2a02ea");
        ThreadUtil.sleep(20, TimeUnit.SECONDS);
    }

    @Test
    public void fileSaveTest() throws IOException {
        String sss = "testAppend";

        fileSaveService.saveBytesToFileWithSource(sss.getBytes(StandardCharsets.UTF_8), "C:\\Users\\yuegenhua\\Desktop\\testFile", "test.txt", SourceEnum.Ehentai, true);
    }
}
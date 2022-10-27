package cn.nyaaar.partridgemngservice.service.torrent;

import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.service.torrent.impl.QbittorrentEngine;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author yuegenhua
 * @Version $Id: QbittorrentServiceTest.java, v 0.1 2022-10 10:19 yuegenhua Exp $$
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = PartridgeMngServiceApplication.class)
public class QBitTorrentServiceTest {

    @Autowired
    private QbittorrentEngine qbittorrentService;
    @Autowired
    private TorrentService torrentService;

    @Test
    public void torrentsTest() {
        log.info(qbittorrentService.getTorrents("", "").toString());
        log.info(qbittorrentService.getTorrents("test", "'").toString());
    }

    @Test
    public void getHashTorrent() {
        log.info(qbittorrentService.getTorrents("", "f666815eda76bf931aa1ab69451af1c1071f5cba").toString());
    }

    @Test
    public void addTorrentTest() {
        qbittorrentService.addTorrent("magnet:?xt=urn:btih:f666815eda76bf931aa1ab69451af1c1071f5cba&tr=http%3a%2f%2ft.nyaatracker.com%2fannounce&tr=http%3a%2f%2ftracker.kamigami.org%3a2710%2fannounce&tr=http%3a%2f%2fshare.camoe.cn%3a8080%2fannounce&tr=http%3a%2f%2fopentracker.acgnx.se%2fannounce&tr=http%3a%2f%2fanidex.moe%3a6969%2fannounce&tr=http%3a%2f%2ft.acg.rip%3a6699%2fannounce&tr=https%3a%2f%2ftr.bangumi.moe%3a9696%2fannounce&tr=udp%3a%2f%2ftr.bangumi.moe%3a6969%2fannounce&tr=http%3a%2f%2fopen.acgtracker.com%3a1096%2fannounce&tr=udp%3a%2f%2ftracker.opentrackr.org%3a1337%2fannounce"
                , "test", "tet", "01");
    }

    @Test
    public void getTorrentContents() {
        log.info(qbittorrentService.getTorrentContents("f666815eda76bf931aa1ab69451af1c1071f5cba").toString());
    }

    @Test
    public void setPri() {
        qbittorrentService.setTorrentContentPriority("33316737bd9afec091f389d057ae5613c3aaf06e", 0, 1);
    }


    @Test
    public void deleteTorrent() {
        qbittorrentService.deleteTorrent("33316737bd9afec091f389d057ae5613c3aaf06e", true);
    }

    @Test
    public void add() {
        torrentService.addTorrent("magnet:?xt=urn:btih:f666815eda76bf931aa1ab69451af1c1071f5cba&tr=http%3a%2f%2ft.nyaatracker.com%2fannounce&tr=http%3a%2f%2ftracker.kamigami.org%3a2710%2fannounce&tr=http%3a%2f%2fshare.camoe.cn%3a8080%2fannounce&tr=http%3a%2f%2fopentracker.acgnx.se%2fannounce&tr=http%3a%2f%2fanidex.moe%3a6969%2fannounce&tr=http%3a%2f%2ft.acg.rip%3a6699%2fannounce&tr=https%3a%2f%2ftr.bangumi.moe%3a9696%2fannounce&tr=udp%3a%2f%2ftr.bangumi.moe%3a6969%2fannounce&tr=http%3a%2f%2fopen.acgtracker.com%3a1096%2fannounce&tr=udp%3a%2f%2ftracker.opentrackr.org%3a1337%2fannounce");
    }

    @Test
    public void uriTest() {
        String appid = null;
        String url = UriComponentsBuilder.fromUriString("http://mydomain/api/getToken")
                .queryParam("appid", appid)
                .queryParam("appsecret", "secret123")
                .build().encode().toString();

        log.info(url);
    }

    @Test
    public void getDownloadingTorrent() {
        ThreadLocalUtil.addCurrentUser("test");
        log.info(torrentService.getDownloadingTorrents().toString());
    }
}
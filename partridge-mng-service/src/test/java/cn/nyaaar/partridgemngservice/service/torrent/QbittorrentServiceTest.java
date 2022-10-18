package cn.nyaaar.partridgemngservice.service.torrent;

import cn.nyaaar.partridgemngservice.PartridgeMngServiceApplication;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.service.torrent.impl.QbittorrentEngine;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yuegenhua
 * @Version $Id: QbittorrentServiceTest.java, v 0.1 2022-10 10:19 yuegenhua Exp $$
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = PartridgeMngServiceApplication.class)
public class QbittorrentServiceTest {

    @Autowired
    private QbittorrentEngine qbittorrentService;
    @Autowired
    private TorrentService torrentService;

    @Test
    public void torrentsTest() {
        log.info(qbittorrentService.getTorrents("").toString());
        log.info(qbittorrentService.getTorrents("test").toString());
    }

    @Test
    public void addTorrentTest() {
        qbittorrentService.addTorrent("magnet:?xt=urn:btih:33316737bd9afec091f389d057ae5613c3aaf06e&tr=http%3a%2f%2ft.nyaatracker.com%2fannounce&tr=http%3a%2f%2ftracker.kamigami.org%3a2710%2fannounce&tr=http%3a%2f%2fshare.camoe.cn%3a8080%2fannounce&tr=http%3a%2f%2fopentracker.acgnx.se%2fannounce&tr=http%3a%2f%2fanidex.moe%3a6969%2fannounce&tr=http%3a%2f%2ft.acg.rip%3a6699%2fannounce&tr=https%3a%2f%2ftr.bangumi.moe%3a9696%2fannounce&tr=udp%3a%2f%2ftr.bangumi.moe%3a6969%2fannounce&tr=http%3a%2f%2fopen.acgtracker.com%3a1096%2fannounce&tr=udp%3a%2f%2ftracker.opentrackr.org%3a1337%2fannounce"
                , "test", new Element()
                        .setType(SourceEnum.Ehentai.getCode())
                        .setId(1L));
    }

    @Test
    public void getTorrentContents() {
        log.info(qbittorrentService.getTorrentContents("33316737bd9afec091f389d057ae5613c3aaf06e").toString());
    }


    @Test
    public void setPri() {
        qbittorrentService.setTorrentContentPriority("33316737bd9afec091f389d057ae5613c3aaf06e", 0, 1);
    }


    @Test
    public void deleteTorrent() {
        qbittorrentService.deleteTorrent("33316737bd9afec091f389d057ae5613c3aaf06e");
    }

    @Test
    public void add() {
        torrentService.addTorrent("magnet:?xt=urn:btih:b34f507e1ba7f7877a159ff730a27c8401b926a8&tr=http%3a%2f%2ft.nyaatracker.com%2fannounce&tr=http%3a%2f%2ftracker.kamigami.org%3a2710%2fannounce&tr=http%3a%2f%2fshare.camoe.cn%3a8080%2fannounce&tr=http%3a%2f%2fopentracker.acgnx.se%2fannounce&tr=http%3a%2f%2fanidex.moe%3a6969%2fannounce&tr=http%3a%2f%2ft.acg.rip%3a6699%2fannounce&tr=https%3a%2f%2ftr.bangumi.moe%3a9696%2fannounce&tr=udp%3a%2f%2ftr.bangumi.moe%3a6969%2fannounce&tr=http%3a%2f%2fopen.acgtracker.com%3a1096%2fannounce&tr=udp%3a%2f%2ftracker.opentrackr.org%3a1337%2fannounce", SourceEnum.Jav);
    }
}
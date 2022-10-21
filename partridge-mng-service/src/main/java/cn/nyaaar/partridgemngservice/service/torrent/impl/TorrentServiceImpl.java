package cn.nyaaar.partridgemngservice.service.torrent.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.EleTorrent;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.qbittorrent.QBitTorrent;
import cn.nyaaar.partridgemngservice.model.qbittorrent.TorrentResp;
import cn.nyaaar.partridgemngservice.service.EleTorrentService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.torrent.TorrentService;
import cn.nyaaar.partridgemngservice.util.StringUtils;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: TorrentServiceImpl.java, v 0.1 2022-13 14:06 yuegenhua Exp $$
 */
@Service
@Slf4j
public class TorrentServiceImpl implements TorrentService {
    private final ElementService elementService;
    private final QbittorrentEngine qbittorrentEngine;
    private final EleTorrentService eleTorrentService;

    public TorrentServiceImpl(ElementService elementService,
                              QbittorrentEngine qbittorrentEngine,
                              EleTorrentService eleTorrentService) {
        this.elementService = elementService;
        this.qbittorrentEngine = qbittorrentEngine;
        this.eleTorrentService = eleTorrentService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTorrent(String torrent, SourceEnum sourceEnum) {
        String hash = parseHashFromTorrent(torrent);
        log.info("[{}] torrent download begin...", hash);
        String userName = ThreadLocalUtil.getCurrentUser();
        Element element = new Element()
                .setType(sourceEnum.getCode())
                .setUploader(userName)
                .setFileSize(0L)
                .setAvailableFlag(PrConstant.VALIDATED)
                .setSharedFlag(PrConstant.NO);
        elementService.save(element);
        qbittorrentEngine.addTorrent(torrent, userName, element);
        EleTorrent eleTorrent = new EleTorrent()
                .setHash(hash)
                .setEleId(element.getId());
        eleTorrentService.save(eleTorrent);
    }

    @Override
    public List<TorrentResp> getDownloadingTorrents() {
        List<QBitTorrent> qBitTorrents = qbittorrentEngine.getUserTorrents(ThreadLocalUtil.getCurrentUser());
        return qBitTorrents.stream()
                .filter(qBitTorrent -> !qBitTorrent.getState().getFinished())
                .map(TorrentResp::new)
                .toList();
    }

    @NotNull
    private static String parseHashFromTorrent(String torrent) {
        String hash = "";
        try {
            hash = torrent.substring(torrent.indexOf("btih:"));
            hash = hash.substring(hash.indexOf(":") + 1, hash.indexOf("&"));
        } catch (Exception ignored) {
        }
        if (StringUtils.isEmpty(hash)) {
            BusinessExceptionEnum.FIELD_ERROR.assertFail("磁力链接解析错误");
        }
        return hash;
    }
}
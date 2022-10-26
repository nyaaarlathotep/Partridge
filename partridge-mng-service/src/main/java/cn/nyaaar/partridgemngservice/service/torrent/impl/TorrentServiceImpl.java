package cn.nyaaar.partridgemngservice.service.torrent.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.EleTorrent;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.qbittorrent.QBitTorrent;
import cn.nyaaar.partridgemngservice.model.qbittorrent.QBitTorrentContent;
import cn.nyaaar.partridgemngservice.model.qbittorrent.TorrentResp;
import cn.nyaaar.partridgemngservice.service.EleTorrentService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.torrent.TorrentService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.StringUtils;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public void addTorrent(String torrent) {
        String hash = parseHashFromTorrent(torrent);
        log.info("[{}] torrent download begin...", hash);
        String userName = ThreadLocalUtil.getCurrentUser();
        Element element = new Element()
                .setType(SourceEnum.Unknown.getCode())
                .setUploader(userName)
                .setFileSize(0L)
                .setAvailableFlag(PrConstant.VALIDATED)
                .setSharedFlag(PrConstant.NO);
        elementService.save(element);
        qbittorrentEngine.addTorrent(torrent, userName, getDownloadDir(userName, element), element.getType());
        EleTorrent eleTorrent = new EleTorrent()
                .setHash(hash)
                .setEleId(element.getId());
        eleTorrentService.save(eleTorrent);
    }

    @Override
    public void addTorrent(Element element, String torrent) {
        String hash = parseHashFromTorrent(torrent);
        log.info("[{}] torrent download begin...", hash);
        String userName = ThreadLocalUtil.getCurrentUser();
        qbittorrentEngine.addTorrent(torrent, userName, getDownloadDir(userName, element), element.getType());
        EleTorrent eleTorrent = new EleTorrent()
                .setHash(hash)
                .setEleId(element.getId());
        eleTorrentService.save(eleTorrent);
    }

    @Override
    public List<TorrentResp> getDownloadingTorrents() {
        List<QBitTorrent> qBitTorrents = qbittorrentEngine.getTorrents(ThreadLocalUtil.getCurrentUser(), "");
        updateEleTorrents(qBitTorrents);
        return qBitTorrents.stream()
                .filter(qBitTorrent -> !qBitTorrent.getState().getFinished())
                .map(TorrentResp::new)
                .toList();
    }


    @Override
    public List<TorrentResp> getTorrents() {
        List<QBitTorrent> qBitTorrents = qbittorrentEngine.getTorrents(ThreadLocalUtil.getCurrentUser(), "");
        updateEleTorrents(qBitTorrents);
        return qBitTorrents.stream()
                .map(TorrentResp::new)
                .toList();
    }

    @Override
    public List<QBitTorrentContent> getTorrentContent(Long eleId) {
        EleTorrent eleTorrent = eleTorrentService.getOne(Wrappers.lambdaQuery(EleTorrent.class)
                .eq(EleTorrent::getEleId, eleId));
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(eleTorrent, "磁链");
        return qbittorrentEngine.getTorrentContents(eleTorrent.getHash());
    }

    @Override
    public void setContentPriority(Long eleId, Integer contentIndex, Integer priority) {
        EleTorrent eleTorrent = eleTorrentService.getOne(Wrappers.lambdaQuery(EleTorrent.class)
                .eq(EleTorrent::getEleId, eleId));
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(eleTorrent, "磁链");
        qbittorrentEngine.setTorrentContentPriority(eleTorrent.getHash(), contentIndex, priority);
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

    private void updateEleTorrents(List<QBitTorrent> qBitTorrents) {
        eleTorrentService.updateBatchById(eleTorrentService.list(Wrappers.lambdaQuery(EleTorrent.class)
                        .in(EleTorrent::getHash, qBitTorrents.stream().map(QBitTorrent::getHash).toList()))
                .parallelStream()
                .map(eleTorrent -> {
                    Optional<QBitTorrent> qBitTorrentOptional = qBitTorrents.stream()
                            .filter(qBitTorrent -> qBitTorrent.getHash().equals(eleTorrent.getHash()))
                            .findFirst();
                    if (qBitTorrentOptional.isPresent() &&
                            !qBitTorrentOptional.get().equalElementTorrent(eleTorrent)) {
                        eleTorrent.setSize(qBitTorrentOptional.get().getSize());
                        eleTorrent.setName(qBitTorrentOptional.get().getName());
                        eleTorrent.setState(qBitTorrentOptional.get().getState().getCode());
                        log.error("[{}] torrent info update!", eleTorrent.getHash());
                        return eleTorrent;
                    }
                    log.error("[{}] torrent not found!", eleTorrent.getHash());
                    return null;
                }).filter(Objects::nonNull)
                .toList());
    }

    private static String getDownloadDir(String userName, Element element) {
        return FileUtil.simpleConcatPath(Settings.getDownloadRootPath(),
                userName, element.getType(), String.valueOf(element.getId()));
    }
}
package cn.nyaaar.partridgemngservice.service.torrent.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.common.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.EleFile;
import cn.nyaaar.partridgemngservice.entity.EleTorrent;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.torrent.QBitTorrent;
import cn.nyaaar.partridgemngservice.model.torrent.QBitTorrentContent;
import cn.nyaaar.partridgemngservice.model.torrent.TorrentResp;
import cn.nyaaar.partridgemngservice.service.EleTorrentService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.torrent.TorrentService;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.StringUtils;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

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
    private final AppUserService appUserService;

    public TorrentServiceImpl(ElementService elementService,
                              QbittorrentEngine qbittorrentEngine,
                              EleTorrentService eleTorrentService, AppUserService appUserService) {
        this.elementService = elementService;
        this.qbittorrentEngine = qbittorrentEngine;
        this.eleTorrentService = eleTorrentService;
        this.appUserService = appUserService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTorrent(String torrent) {
        checkQuota();
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
        checkQuota();
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
        return getTorrentResps(qBitTorrent -> !qBitTorrent.getState().getFinished());
    }

    @NotNull
    private List<TorrentResp> getTorrentResps(Predicate<QBitTorrent> predicate) {
        String userName = ThreadLocalUtil.getCurrentUser();
        if (appUserService.isRoot(userName)) {
            userName = "";
        }
        List<QBitTorrent> qBitTorrents = qbittorrentEngine.getTorrents(userName, "");
        List<EleTorrent> eleTorrents = eleTorrentService.list(Wrappers.lambdaQuery(EleTorrent.class)
                .in(EleTorrent::getHash, qBitTorrents.stream().map(QBitTorrent::getHash).toList()));
        updateEleTorrents(qBitTorrents, eleTorrents);
        return qBitTorrents.stream()
                .filter(predicate)
                .map(qBitTorrent -> {
                    Optional<EleTorrent> optionalEleTorrent = eleTorrents.stream()
                            .filter(eleTorrent -> eleTorrent.getHash().equals(qBitTorrent.getHash()))
                            .findFirst();
                    if (optionalEleTorrent.isPresent()) {
                        return new TorrentResp(qBitTorrent, optionalEleTorrent.get());
                    }
                    log.error("[{}] torrent not found!", qBitTorrent.getHash());
                    return null;
                }).filter(Objects::nonNull)
                .toList();
    }


    @Override
    public List<TorrentResp> getTorrents() {
        return getTorrentResps((qBitTorrent) -> true);
    }

    @Override
    public List<QBitTorrentContent> getTorrentContent(String hash) {
        EleTorrent eleTorrent = getEleTorrent(hash);
        return qbittorrentEngine.getTorrentContents(eleTorrent.getHash());
    }

    @Override
    public void setContentPriority(String hash, Integer contentIndex, Integer priority) {
        EleTorrent eleTorrent = getEleTorrent(hash);
        qbittorrentEngine.setTorrentContentPriority(eleTorrent.getHash(), contentIndex, priority);
    }

    @Override
    public void deleteTorrentContent(String hash, Integer contentIndex) {
        EleTorrent eleTorrent = getEleTorrent(hash);
        List<QBitTorrentContent> qBitTorrentContents = qbittorrentEngine.getTorrentContents(hash);
        qbittorrentEngine.setTorrentContentPriority(eleTorrent.getHash(), contentIndex, 0);
        QBitTorrent qBitTorrent = getQBitTorrentByHash(hash);
        Optional<QBitTorrentContent> qBitTorrentContent = qBitTorrentContents.stream()
                .filter(qBitTorrentContentFilter -> qBitTorrentContentFilter.getIndex().equals(Long.valueOf(contentIndex)))
                .findFirst();
        if (qBitTorrentContent.isPresent()) {

            String path = FileUtil.simpleConcatPath(qBitTorrent.getSave_path().replace(Settings.getQbittorrentPath(),
                    Settings.getQbittorrentHostPath()), qBitTorrentContent.get().getName());
            boolean deleteSuccess = FileUtil.delete(path);
            if (!deleteSuccess) {
                log.warn("[{}], contentIndex: [{}], path: [{}], delete error!", hash, contentIndex, path);
            } else {
                log.info("[{}], contentIndex: [{}], path: [{}], delete success", hash, contentIndex, path);
            }
        } else {
            BusinessExceptionEnum.NOT_FOUND.assertFail("磁链内容");
        }
    }

    @Override
    public void deleteTorrent(String hash) {
        EleTorrent eleTorrent = getEleTorrent(hash);
        qbittorrentEngine.deleteTorrent(hash, true);
        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getAvailableFlag, PrConstant.INVALIDATED)
                .eq(Element::getId, eleTorrent.getEleId()));
    }

    @Override
    public void callBack(String hash) {
        log.info("[{}] torrent download complete!", hash);
        EleTorrent eleTorrent = getEleTorrent(hash);
        QBitTorrent qBitTorrent = getQBitTorrentByHash(hash);
        updateEleTorrents(Collections.singletonList(qBitTorrent), Collections.singletonList(eleTorrent));
        List<QBitTorrentContent> contents = getTorrentContent(hash);
        for (QBitTorrentContent content : contents) {
            String fileName = FileUtil.getFileNameFromPath(content.getName());
            FileTypeEnum fileTypeEnum = FileTypeEnum.getTypeBySuffix(fileName);
            EleFile eleFile = new EleFile();
            eleFile.setEleId(eleTorrent.getEleId());
            eleFile.setName(fileName);
            // TODO path convert
            eleFile.setPath(FileUtil.simpleConcatPath(qBitTorrent.getSave_path(), content.getName()));
            eleFile.setAvailableFlag(PrConstant.VALIDATED);
            eleFile.setCompletedFlag(PrConstant.YES);
            eleFile.setType(fileTypeEnum.getCode());
        }
    }

    private QBitTorrent getQBitTorrentByHash(String hash) {
        List<QBitTorrent> qBitTorrents = qbittorrentEngine.getTorrents("", hash);
        if (qBitTorrents.size() < 1) {
            BusinessExceptionEnum.NOT_FOUND.assertFail("qbittorrent 未找到对应磁链，请联系管理员");
        }
        return qBitTorrents.get(0);
    }

    private EleTorrent getEleTorrent(String hash) {
        EleTorrent eleTorrent = eleTorrentService.getById(hash);
        BusinessExceptionEnum.NOT_FOUND.assertNotNull(eleTorrent, "磁链");
        return eleTorrent;
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

    private void updateEleTorrents(List<QBitTorrent> qBitTorrents, List<EleTorrent> eleTorrents) {
        eleTorrentService.updateBatchById(eleTorrents
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
        return FileUtil.simpleConcatPath(Settings.getQbittorrentPath(),
                userName, element.getType(), String.valueOf(element.getId()));
    }

    private void checkQuota() {
        List<TorrentResp> downloadingTorrents = getDownloadingTorrents();
        if (downloadingTorrents.size() >= Settings.getTorrentMax()) {
            BusinessExceptionEnum.USER_CUSTOM.assertFail("已存在 [" + downloadingTorrents.size() + "] 个正在下载的磁链，请等待下载完成。");
        }
        BusinessExceptionEnum.SPACE_INSUFFICIENT.assertIsTrue(appUserService.checkUserSpaceLimit(ThreadLocalUtil.getCurrentUser()));
    }
}
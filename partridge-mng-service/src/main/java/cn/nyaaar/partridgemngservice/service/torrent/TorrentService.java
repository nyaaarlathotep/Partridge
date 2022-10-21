package cn.nyaaar.partridgemngservice.service.torrent;

import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.model.qbittorrent.TorrentResp;

import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: TorrentService.java, v 0.1 2022-13 14:06 yuegenhua Exp $$
 */
public interface TorrentService {

    void addTorrent(String torrent, SourceEnum sourceEnum);
    
    List<TorrentResp> getDownloadingTorrents();
}
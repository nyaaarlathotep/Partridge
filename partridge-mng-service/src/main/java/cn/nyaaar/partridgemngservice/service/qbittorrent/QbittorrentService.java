package cn.nyaaar.partridgemngservice.service.qbittorrent;

import cn.nyaaar.partridgemngservice.model.qbittorrent.Torrent;

import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: QbittorrentService.java, v 0.1 2022-10 9:46 yuegenhua Exp $$
 */
public interface QbittorrentService {

    List<Torrent> getTorrents();

}
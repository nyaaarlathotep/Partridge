package cn.nyaaar.partridgemngservice.service.torrent;

import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.model.qbittorrent.QBitTorrentContent;
import cn.nyaaar.partridgemngservice.model.qbittorrent.TorrentResp;

import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: TorrentService.java, v 0.1 2022-13 14:06 yuegenhua Exp $$
 */
public interface TorrentService {

    /**
     * 添加磁链
     *
     * @param torrent    torrent
     * @param sourceEnum sourceEnum
     */
    void addTorrent(String torrent, SourceEnum sourceEnum);

    /**
     * 添加磁链
     *
     * @param torrent    torrent
     * @param element    element
     * @param sourceEnum sourceEnum
     */
    void addTorrent(Element element, String torrent, SourceEnum sourceEnum);

    /**
     * 获取当前用户正在下载的磁链，不包含已经完成下载的
     *
     * @return List<TorrentResp>
     */
    List<TorrentResp> getDownloadingTorrents();

    /**
     * 获取当前用户所有的磁链
     *
     * @return List<TorrentResp>
     */
    List<TorrentResp> getTorrents();

    /**
     * 获取 eleId 对应磁链的所有内容
     *
     * @param eleId eleId
     * @return List<QBitTorrentContent>
     */
    List<QBitTorrentContent> getTorrentContent(Long eleId);

    /**
     * 设置 eleId 对应磁链某个内容的下载优先级
     *
     * @param eleId        eleId
     * @param contentIndex contentIndex
     * @param priority     priority，0 为不下载，1 为正常下载，2更高，以此类推
     */
    void setContentPriority(Long eleId, Integer contentIndex, Integer priority);
}
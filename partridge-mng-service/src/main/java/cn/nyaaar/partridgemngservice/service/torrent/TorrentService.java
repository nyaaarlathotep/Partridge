package cn.nyaaar.partridgemngservice.service.torrent;

import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.model.torrent.QBitTorrentContent;
import cn.nyaaar.partridgemngservice.model.torrent.TorrentResp;

import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: TorrentService.java, v 0.1 2022-13 14:06 yuegenhua Exp $$
 */
public interface TorrentService {

    /**
     * 添加磁链
     *
     * @param torrent torrent
     */
    void addTorrent(String torrent);

    /**
     * 添加磁链
     *
     * @param element element
     * @param torrent torrent
     */
    void addTorrent(Element element, String torrent);

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
     * @param hash hash
     * @return List<QBitTorrentContent>
     */
    List<QBitTorrentContent> getTorrentContent(String hash);

    /**
     * 设置 eleId 对应磁链某个内容的下载优先级
     *
     * @param hash         hash
     * @param contentIndex contentIndex
     * @param priority     priority，0 为不下载，1 为正常下载，2更高，以此类推
     */
    void setContentPriority(String hash, Integer contentIndex, Integer priority);

    /**
     * 删除 torrent 某个内容
     *
     * @param hash         hash
     * @param contentIndex contentIndex
     */
    void deleteTorrentContent(String hash, Integer contentIndex);


    /**
     * 删除 torrent 
     *
     * @param hash         hash
     */
    void deleteTorrentContent(String hash);
}
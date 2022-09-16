package cn.nyaaar.partridgemngservice.service.ehService;

import cn.nyaaar.partridgemngservice.model.eh.*;
import cn.nyaaar.partridgemngservice.model.ListResp;

import java.util.List;
import java.util.Map;

/**
 * @author nyaaar
 * @Version $Id: EhService.java, v 0.1 2022-31 14:31 nyaaar Exp $$
 */
public interface EhService {

    /**
     * 通过画廊id和页码获得base64的图片
     *
     * @param gid       gid
     * @param pageIndex 页码
     * @return base64图片
     */
    String getGalleryPage(long gid, int pageIndex);

    /**
     * 通过画廊id和页码获得base64的图片
     *
     * @param eleId       eleId
     * @param pageIndexes 画廊图片页码
     * @return base64图片
     */
    List<GalleryPage> getGalleryPage(long eleId, List<Integer> pageIndexes);

    /**
     * 下载画廊
     *
     * @param gid    画廊id
     * @param gtoken gtoken
     */
    void downloadGallery(long gid, String gtoken);

    /**
     * 下载画廊的指定页数
     *
     * @param gid         gid
     * @param gtoken      gtoken
     * @param pageIndexes 画廊页数
     * @return GalleryPage List
     */
    List<GalleryPage> downloadGalleryPages(long gid, String gtoken, List<Integer> pageIndexes);

    /**
     * 返回当前画廊下载队列
     * key: gid
     * value: gallery download status
     *
     * @return queue
     */
    Map<Long, DownloadingGallery> getDownloadingQueue();

    /**
     * eleId->GalleryBasicInfo
     *
     * @param eleId eleId
     * @return GalleryBasicInfo
     */
    GalleryBasicInfo getGalleryBasicByGid(long eleId);

    /**
     * 浏览 gallery，返回一个 GalleryBasicInfo 的 list
     *
     * @param pageIndex pageIndex
     * @return ListResp
     */
    ListResp<GalleryBasicInfo> getGalleryList(int pageIndex);

    /**
     * 搜索符合相关条件的 ehentai gallery
     *
     * @param galleryQuery galleryQuery
     * @param pageIndex    pageIndex
     * @return ListResp
     */
    ListResp<GalleryBasicInfo> getGalleryList(GalleryQuery galleryQuery, int pageIndex);

    /**
     * gid+gtoken->GalleryDetail
     *
     * @param gid gid
     * @return GalleryDetail
     */
    GalleryDetail getGalleryDetailByGid(long gid, String gtoken);
}
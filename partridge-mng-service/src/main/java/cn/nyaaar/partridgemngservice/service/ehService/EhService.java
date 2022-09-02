package cn.nyaaar.partridgemngservice.service.ehService;

import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: EhService.java, v 0.1 2022-31 14:31 yuegenhua Exp $$
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
     */
    void downloadGalleryPages(long gid, String gtoken, List<Integer> pageIndexes);
}
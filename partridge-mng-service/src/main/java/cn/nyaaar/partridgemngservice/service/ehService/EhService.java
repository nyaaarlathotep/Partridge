package cn.nyaaar.partridgemngservice.service.ehService;

/**
 * @author yuegenhua
 * @Version $Id: EhService.java, v 0.1 2022-31 14:31 yuegenhua Exp $$
 */
public interface EhService {
    
    String getGalleryPage(long gid,int pageIndex);

    void downloadGallery(long gid, String gtoken);
}
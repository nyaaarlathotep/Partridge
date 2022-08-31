package cn.nyaaar.partridgemngservice.service.download;

import org.springframework.lang.NonNull;

/**
 * @author yuegenhua
 * @Version $Id: DownloadService.java, v 0.1 2022-31 17:00 yuegenhua Exp $$
 */
public interface DownloadService {

    void downloadUrlToDest(String url, String dest,String fileName);
}
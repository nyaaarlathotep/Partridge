package cn.nyaaar.partridgemngservice.service.file;


/**
 * @author nyaaar
 * @Version $Id: DownloadService.java, v 0.1 2022-31 17:00 nyaaar Exp $$
 */
public interface DownloadService {

    void downloadUrlToDest(String url, String dest, String fileName, Runnable successHandle, Runnable failHandle, boolean reDownload);

    String downloadUrlToBase64(String url);
}
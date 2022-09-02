package cn.nyaaar.partridgemngservice.service.file;


import java.io.IOException;

/**
 * @author yuegenhua
 * @Version $Id: FileService.java, v 0.1 2022-01 9:37 yuegenhua Exp $$
 */
public interface FileHandleService {

    void saveBytesToFileWithSource(byte[] bytes, String destDic, String fileName, boolean reDownload) throws IOException;
}
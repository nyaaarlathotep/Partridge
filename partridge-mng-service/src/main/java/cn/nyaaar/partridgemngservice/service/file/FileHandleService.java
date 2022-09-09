package cn.nyaaar.partridgemngservice.service.file;


import java.io.IOException;

/**
 * @author nyaaar
 * @Version $Id: FileService.java, v 0.1 2022-01 9:37 nyaaar Exp $$
 */
public interface FileHandleService {

    void saveBytesToFile(byte[] bytes, String destDic, String fileName, boolean reDownload) throws IOException;
}
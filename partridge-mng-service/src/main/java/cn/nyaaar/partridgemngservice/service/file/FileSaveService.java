package cn.nyaaar.partridgemngservice.service.file;

import cn.nyaaar.partridgemngservice.enums.SourceEnum;

import java.io.IOException;

/**
 * @author yuegenhua
 * @Version $Id: FileService.java, v 0.1 2022-01 9:37 yuegenhua Exp $$
 */
public interface FileSaveService {

    void saveBytesToFileWithSource(byte[] bytes, String destDic, String fileName, SourceEnum sourceEnum, boolean reDownload) throws IOException;
}
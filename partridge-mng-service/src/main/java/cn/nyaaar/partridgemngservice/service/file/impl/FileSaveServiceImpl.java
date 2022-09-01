package cn.nyaaar.partridgemngservice.service.file.impl;

import cn.nyaaar.partridgemngservice.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.service.file.FileSaveService;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * @author yuegenhua
 * @Version $Id: FileSaveServiceImpl.java, v 0.1 2022-01 10:38 yuegenhua Exp $$
 */
@Service
public class FileSaveServiceImpl implements FileSaveService {
    @Override
    public void saveBytesToFileWithSource(byte[] bytes, String destDic, String fileName, SourceEnum sourceEnum, boolean reDownload) throws IOException {

        Path dic = Path.of(destDic);
        if (Files.notExists(dic)) {
            Files.createDirectories(dic);
        }
        Path filePath = Path.of(PathUtil.simpleConcatUrl(destDic, fileName));
        if (reDownload || Files.notExists(filePath)) {
            Files.write(filePath, bytes, StandardOpenOption.CREATE);
        }
    }
}
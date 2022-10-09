package cn.nyaaar.partridgemngservice.service.file;

import cn.nyaaar.partridgemngservice.entity.EleFile;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;

import java.io.IOException;

/**
 * @author yuegenhua
 * @Version $Id: UploadService.java, v 0.1 2022-29 10:14 yuegenhua Exp $$
 */
public interface UploadService {

    /**
     * 检查文件是否分片是否上传完成，如果完成，则进行合并。
     *
     * @param fileName     fileName
     * @param fileMd5      fileMd5
     * @param fileSize     fileSize
     * @param eleFile    eleFile
     * @param uploaderPath uploaderPath
     * @return 缺失分片序号
     * @throws IOException 文件操作异常
     */
    CheckResp check(String fileName, String fileMd5, Long fileSize, EleFile eleFile, String uploaderPath) throws IOException;

    /**
     * 检查文件是否分片是否上传完成，如果完成，则进行合并，只能在后面几次调用
     *
     * @param eleFileId eleFileId
     * @return 缺失分片序号
     */
    CheckResp check(Long eleFileId);

    /**
     * 上传文件分片
     *
     * @param shardIndex shardIndex
     * @param fileMd5    fileMd5
     * @param shardMd5   shardMd5
     * @param shardBytes shardBytes
     */
    void upload(Integer shardIndex, String fileMd5, String shardMd5, byte[] shardBytes);
}
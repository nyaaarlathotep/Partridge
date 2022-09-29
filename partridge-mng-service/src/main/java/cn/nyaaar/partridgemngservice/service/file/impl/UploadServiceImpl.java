package cn.nyaaar.partridgemngservice.service.file.impl;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.common.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.entity.EleFile;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.entity.FileUploadInfo;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.service.EleFileService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.FileUploadInfoService;
import cn.nyaaar.partridgemngservice.service.file.UploadService;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author yuegenhua
 * @Version $Id: UploadServiceImpl.java, v 0.1 2022-29 9:07 yuegenhua Exp $$
 */
@Service
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final FileUploadInfoService fileUploadInfoService;
    private final ElementService elementService;
    private final EleFileService eleFileService;

    public UploadServiceImpl(FileUploadInfoService fileUploadInfoService, ElementService elementService, EleFileService eleFileService) {
        this.fileUploadInfoService = fileUploadInfoService;
        this.elementService = elementService;
        this.eleFileService = eleFileService;
    }


    public CheckResp check(String fileName, String fileMd5, Long fileSize, Integer eleFileId) throws IOException {
        EleFile eleFile = eleFileService.findById(eleFileId);
        Element element = elementService.getById(eleFile.getEleId());
        File filePath = new File(getDownloadDir(ThreadLocalUtil.getCurrentUser(), element), fileMd5);
        File currentFile = new File(filePath, fileName);
        if (currentFile.exists()) {
            return new CheckResp();
        }
        if (!filePath.exists()) {
            Files.createDirectories(filePath.toPath());
        }
        FileUploadInfo fileUploadInfo = fileUploadInfoService.getOne(Wrappers.lambdaQuery(FileUploadInfo.class)
                .eq(FileUploadInfo::getFileKey, fileMd5));
        if (fileUploadInfo == null) {
            int shardTotal = (int) Math.ceil(1.0 * fileSize / Settings.getShardSize());
            fileUploadInfo = new FileUploadInfo()
                    .setFileKey(fileMd5)
                    .setPath(currentFile.getAbsolutePath())
                    .setName(fileName)
                    .setShardNum(0)
                    .setShardSize(Settings.getShardSize())
                    .setSuffix(FileTypeEnum.getTypeBySuffix(fileName).getSuffix())
                    .setShardTotal(shardTotal)
                    .setSize(fileSize)
                    .setUploadFlag(PrConstant.NEW_CREATED);
            fileUploadInfoService.save(fileUploadInfo);
        }

        Set<Integer> chunks = IntStream.range(0, fileUploadInfo.getShardTotal()).boxed().collect(Collectors.toSet());

        Set<Integer> chunkNames = new TreeSet<>();
        try (Stream<Path> fileNameStream = Files.walk(filePath.toPath())) {
            chunkNames = fileNameStream
                    .filter(Files::isRegularFile)
                    .map(path -> Integer.parseInt(path.getFileName().toString()))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("[{}]read file shards fail, ", ThreadLocalUtil.getCurrentUser(), e);
            BusinessExceptionEnum.SYSTEM_DATA_ERROR.assertFail("读取分片文件出错，请联系管理员");
        }
        // 得到缺失的文件片号
        chunks.removeAll(chunkNames);
        if (chunks.isEmpty()) {
            merge(fileUploadInfo, eleFileId);
        }
        return new CheckResp()
                .setMissingShardIndex(chunks)
                .setShardSize(fileUploadInfo.getShardSize());
    }

    public void upload(Integer shardIndex, String fileMd5, String shardMd5, byte[] shardBytes, Integer eleFileId) throws IOException {
        log.info("{} 分片:[{}]上传开始", fileMd5, shardIndex);
        BusinessExceptionEnum.VERIFY_MD5_ERR.assertIsTrue(shardMd5.equals(DigestUtils.md5DigestAsHex(shardBytes)));

        EleFile eleFile = eleFileService.findById(eleFileId);
        Element element = elementService.getById(eleFile.getEleId());
        File filePath = new File(getDownloadDirChild(ThreadLocalUtil.getCurrentUser(), element, fileMd5), String.valueOf(shardIndex));
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            StreamUtils.copy(shardBytes, fileOutputStream);
        } catch (Exception e) {
            // 分片传输过程中出现问题,删除当前分片文件
            Files.delete(filePath.toPath());
            throw e;
        }
        FileUploadInfo fileUploadInfo = fileUploadInfoService.getOne(Wrappers.lambdaQuery(FileUploadInfo.class)
                .eq(FileUploadInfo::getFileKey, fileMd5));
        fileUploadInfo.setShardNum(fileUploadInfo.getShardNum() + 1);
        fileUploadInfoService.updateById(fileUploadInfo);
        if (fileUploadInfo.getShardNum().equals(fileUploadInfo.getShardTotal())) {
            merge(fileUploadInfo, eleFileId);
        }
        log.info("{} 分片:[{}]上传成功", fileMd5, shardIndex);
    }

    private void merge(FileUploadInfo fileUploadInfo, Integer eleFileId) throws IOException {
        log.info("{} 合并开始", fileUploadInfo.getFileKey());

        EleFile eleFile = eleFileService.findById(eleFileId);
        Element element = elementService.getById(eleFile.getEleId());
        File filePath = new File(getDownloadDir(ThreadLocalUtil.getCurrentUser(), element), fileUploadInfo.getFileKey());
        File currentFile = new File(filePath, fileUploadInfo.getName());
        if (currentFile.exists()) {
            return;
        }
        List<Path> shardFiles;
        try (Stream<Path> paths = Files.walk(filePath.toPath())) {
            shardFiles = paths
                    .filter(Files::isRegularFile)
                    .sorted()
                    .toList();
        }
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(currentFile))) {
            for (Path shardFile : shardFiles) {
                bufferedOutputStream.write(Files.readAllBytes(shardFile));
            }
        }
        for (Path shardFile : shardFiles) {
            Files.delete(shardFile);
        }
        fileUploadInfo.setUploadFlag(PrConstant.UPLOADED);
        fileUploadInfoService.updateById(fileUploadInfo);
        log.info("{} 合并成功", fileUploadInfo.getFileKey());
    }

    private static String getDownloadDir(String userName, Element element) {
        return PathUtil.simpleConcatUrl(Settings.getDownloadRootPath(),
                userName, element.getType(), String.valueOf(element.getId()));
    }

    private static String getDownloadDirChild(String userName, Element element, String... names) {
        String[] dirs = new String[names.length + 1];
        dirs[0] = getDownloadDir(userName, element);
        System.arraycopy(names, 0, dirs, 1, names.length);
        return PathUtil.simpleConcatUrl(dirs);
    }
}
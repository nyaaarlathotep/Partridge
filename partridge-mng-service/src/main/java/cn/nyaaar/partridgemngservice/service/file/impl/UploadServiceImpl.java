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
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.PathUtil;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

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
    private final AppUserService appUserService;

    public UploadServiceImpl(FileUploadInfoService fileUploadInfoService,
                             ElementService elementService,
                             EleFileService eleFileService,
                             AppUserService appUserService) {
        this.fileUploadInfoService = fileUploadInfoService;
        this.elementService = elementService;
        this.eleFileService = eleFileService;
        this.appUserService = appUserService;
    }


    @Transactional(rollbackFor = Exception.class)
    public CheckResp check(String fileName, String fileMd5, Long fileSize, Integer eleFileId, String uploaderPath) throws IOException {
        EleFile eleFile = eleFileService.findById(eleFileId);
        Element element = elementService.getById(eleFile.getEleId());
        File fileDir = new File(getDownloadDir(ThreadLocalUtil.getCurrentUser(), element), fileMd5);
        File currentFile = new File(fileDir, fileName);
        if (currentFile.exists()) {
            return new CheckResp();
        }
        if (!fileDir.exists()) {
            Files.createDirectories(fileDir.toPath());
        }
        FileUploadInfo fileUploadInfo = fileUploadInfoService.getOne(Wrappers.lambdaQuery(FileUploadInfo.class)
                .eq(FileUploadInfo::getFileKey, fileMd5));
        if (fileUploadInfo == null) {
            int shardTotal = (int) Math.ceil(1.0 * fileSize / Settings.getShardSize());
            fileUploadInfo = new FileUploadInfo()
                    .setFileKey(fileMd5)
                    .setEleFileId(eleFileId)
                    .setPath(currentFile.getAbsolutePath())
                    .setUploaderPath(uploaderPath)
                    .setName(fileName)
                    .setShardNum(0)
                    .setShardSize(Settings.getShardSize())
                    .setSuffix(FileTypeEnum.getTypeBySuffix(fileName).getSuffix())
                    .setShardTotal(shardTotal)
                    .setSize(fileSize)
                    .setUploadFlag(PrConstant.UPLOADING);
            fileUploadInfoService.save(fileUploadInfo);
        }
        return getCheckResp(fileUploadInfo, fileDir.toPath());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckResp check(Integer eleFileId) {
        EleFile eleFile = eleFileService.findById(eleFileId);
        FileUploadInfo fileUploadInfo = fileUploadInfoService.getOne(Wrappers.lambdaQuery(FileUploadInfo.class)
                .eq(FileUploadInfo::getEleFileId, eleFile.getId()));
        BusinessExceptionEnum.NOT_EXISTS.assertNotNull(fileUploadInfo, "fileUploadInfo");
        if (Objects.equals(fileUploadInfo.getUploadFlag(), PrConstant.UPLOADED)) {
            return new CheckResp()
                    .setEleFileId(fileUploadInfo.getEleFileId())
                    .setUploaded(true);
        }
        String fileDIr = FileUtil.getFileDir(fileUploadInfo.getPath());
        try {
            return getCheckResp(fileUploadInfo, Path.of(fileDIr));
        } catch (IOException e) {
            log.error("file check error, ", e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();
        }
        return null;
    }

    private CheckResp getCheckResp(FileUploadInfo fileUploadInfo, Path fileDir) throws IOException {
        Set<Integer> totalShards = IntStream
                .range(0, fileUploadInfo.getShardTotal())
                .boxed()
                .collect(Collectors.toSet());

        Set<Integer> uploadedShards = Collections.emptySet();
        try (Stream<Path> fileNameStream = Files.walk(fileDir)) {
            uploadedShards = fileNameStream
                    .filter(Files::isRegularFile)
                    .map(path -> Integer.parseInt(path.getFileName().toString()))
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("[{}] read file shard name fail, ", fileUploadInfo.getFileKey(), e);
            BusinessExceptionEnum.SYSTEM_DATA_ERROR.assertFail("读取分片文件出错，请联系管理员");
        }
        if (!fileUploadInfo.getShardNum().equals(uploadedShards.size())) {
            log.error("[{}] shardNum mismatch!", fileUploadInfo.getFileKey());
            fileUploadInfoService.update(Wrappers.lambdaUpdate(FileUploadInfo.class)
                    .set(FileUploadInfo::getShardNum, uploadedShards.size())
                    .eq(FileUploadInfo::getId, fileUploadInfo.getId()));
            fileUploadInfo.setShardNum(uploadedShards.size());
        }
        // 得到缺失的文件片号
        totalShards.removeAll(uploadedShards);
        if (totalShards.isEmpty()) {
            merge(fileUploadInfo);
        }
        return new CheckResp()
                .setMissingShardIndex(totalShards)
                .setShardSize(fileUploadInfo.getShardSize())
                .setEleFileId(fileUploadInfo.getEleFileId())
                .setUploaderPath(fileUploadInfo.getUploaderPath())
                .setSize(fileUploadInfo.getSize());
    }

    public void upload(Integer shardIndex, String fileMd5, String shardMd5, byte[] shardBytes) {
        log.info("[{}] 分片:[{}]上传开始", fileMd5, shardIndex);
        checkShardMd5(shardMd5, shardBytes);

        FileUploadInfo fileUploadInfo = fileUploadInfoService.getOne(Wrappers.lambdaQuery(FileUploadInfo.class)
                .eq(FileUploadInfo::getFileKey, fileMd5));

        BusinessExceptionEnum.NOT_EXISTS.assertNotNull(fileUploadInfo, "fileUploadInfo");
        EleFile eleFile = eleFileService.findById(fileUploadInfo.getEleFileId());
        Element element = elementService.getById(eleFile.getEleId());
        try {
            FileUtil.saveBytesToFile(shardBytes, getDownloadDirChild(ThreadLocalUtil.getCurrentUser(), element, fileMd5),
                    getShardName(shardIndex), true);
            log.info("[{}] 分片:[{}]上传成功", fileMd5, shardIndex);
            fileUploadInfo.setShardNum(fileUploadInfo.getShardNum() + 1);
            fileUploadInfoService.updateById(fileUploadInfo);
            if (fileUploadInfo.getShardNum().equals(fileUploadInfo.getShardTotal())) {
                merge(fileUploadInfo);
            }
        } catch (IOException e) {
            log.error("[{}] 分片:[{}]上传失败", fileMd5, shardIndex, e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();

        }
    }


    private static void checkShardMd5(String shardMd5, byte[] shardBytes) {
        BusinessExceptionEnum.VERIFY_MD5_ERR.assertIsTrue(shardMd5.equals(DigestUtils.md5DigestAsHex(shardBytes)));
    }

    private static String getShardName(Integer shardIndex) {
        return String.valueOf(shardIndex);
    }

    private void merge(FileUploadInfo fileUploadInfo) throws IOException {
        log.info("[{}] 合并开始", fileUploadInfo.getFileKey());

        EleFile eleFile = eleFileService.findById(fileUploadInfo.getEleFileId());
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
        log.info("[{}] 合并成功", fileUploadInfo.getFileKey());
        mergePostHandle(fileUploadInfo, element, eleFile);
    }

    private void mergePostHandle(FileUploadInfo fileUploadInfo, Element element, EleFile eleFile) {
        fileUploadInfoService.update(Wrappers.lambdaUpdate(FileUploadInfo.class)
                .set(FileUploadInfo::getUploadFlag, PrConstant.UPLOADED)
                .eq(FileUploadInfo::getId, fileUploadInfo.getId()));
        eleFileService.update(Wrappers.lambdaUpdate(EleFile.class)
                .set(EleFile::getPath, fileUploadInfo.getPath())
                .set(EleFile::getCompletedFlag, PrConstant.YES)
                .eq(EleFile::getId, eleFile.getId()));
        Long elementBytes = FileUtil.getFolderSize(fileUploadInfo.getPath());
        elementService.update(Wrappers.lambdaUpdate(Element.class)
                .set(Element::getFileSize, element.getFileSize() + elementBytes)
                .eq(Element::getId, element.getId()));
        appUserService.minusUserSpaceLimit(ThreadLocalUtil.getCurrentUser(), elementBytes);
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
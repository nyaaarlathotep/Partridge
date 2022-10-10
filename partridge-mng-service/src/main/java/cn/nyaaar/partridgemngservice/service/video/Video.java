package cn.nyaaar.partridgemngservice.service.video;

import cn.nyaaar.partridgemngservice.common.constants.PrConstant;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import cn.nyaaar.partridgemngservice.common.enums.FileTypeEnum;
import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.EleFile;
import cn.nyaaar.partridgemngservice.entity.Element;
import cn.nyaaar.partridgemngservice.entity.FileUploadInfo;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.file.FileReq;
import cn.nyaaar.partridgemngservice.service.EleFileService;
import cn.nyaaar.partridgemngservice.service.ElementService;
import cn.nyaaar.partridgemngservice.service.FileUploadInfoService;
import cn.nyaaar.partridgemngservice.service.transmit.UploadService;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import cn.nyaaar.partridgemngservice.util.FileUtil;
import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author yuegenhua
 * @Version $Id: Video.java, v 0.1 2022-09 15:30 yuegenhua Exp $$
 */
@Slf4j
public abstract class Video {
    private final ElementService elementService;
    private final AppUserService appUserService;
    private final EleFileService eleFileService;
    private final FileUploadInfoService fileUploadInfoService;
    private final UploadService uploadService;

    protected Video(ElementService elementService,
                    AppUserService appUserService,
                    EleFileService eleFileService,
                    FileUploadInfoService fileUploadInfoService,
                    UploadService uploadService) {
        this.elementService = elementService;
        this.appUserService = appUserService;
        this.eleFileService = eleFileService;
        this.fileUploadInfoService = fileUploadInfoService;
        this.uploadService = uploadService;
    }

    public void checkQuota() {
        String userName = ThreadLocalUtil.getCurrentUser();
        List<FileUploadInfo> uploadingFiles = elementService
                .list(Wrappers.lambdaQuery(Element.class)
                        .eq(Element::getUploader, userName))
                .stream()
                .map(element -> eleFileService.getOne(Wrappers.lambdaQuery(EleFile.class)
                        .eq(EleFile::getEleId, element.getId()))).filter(Objects::nonNull)
                .map(eleFile -> fileUploadInfoService.getOne(Wrappers.lambdaQuery(FileUploadInfo.class)
                        .eq(FileUploadInfo::getEleFileId, eleFile.getId())
                        .eq(FileUploadInfo::getUploadFlag, PrConstant.UPLOADING))).filter(Objects::nonNull)
                .toList();
        if (uploadingFiles.size() >= Settings.getFileUploadingMax()) {
            BusinessExceptionEnum.USER_CUSTOM.assertFail("已存在 " + uploadingFiles.size() + " 个正在上传的文件，请先上传完成。");
        }
        BusinessExceptionEnum.SPACE_INSUFFICIENT.assertIsTrue(appUserService.checkUserSpaceLimit(ThreadLocalUtil.getCurrentUser()));
    }


    public EleFile preUploadHandle(FileReq fileReq) {
        Element element = new Element()
                .setType(SourceEnum.Jav.getCode())
                .setUploader(ThreadLocalUtil.getCurrentUser())
                .setFileSize(0L)
                .setAvailableFlag(PrConstant.VALIDATED)
                .setSharedFlag(PrConstant.NO);
        elementService.save(element);
        EleFile eleFile = new EleFile()
                .setEleId(element.getId())
                .setType(FileTypeEnum.getTypeBySuffix(fileReq.getFileName()).getSuffix())
                .setAvailableFlag(PrConstant.VALIDATED)
                .setName(FileUtil.legalizeFileName(fileReq.getFileName()));
        eleFileService.save(eleFile);
        return eleFile;
    }

    public void postUploadHandle() {
       
    }

    public CheckResp getCheckResp(FileReq fileReq, EleFile eleFile) {
        try {
            return uploadService.check(fileReq.getFileName(), fileReq.getFileMd5(),
                    fileReq.getFileSize(), eleFile, fileReq.getUploaderPath());
        } catch (IOException e) {
            log.error("check error, ", e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();
        }
        return null;
    }
}
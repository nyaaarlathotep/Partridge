package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.file.FileReq;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.validate.FileCheck;
import cn.nyaaar.partridgemngservice.model.validate.FileUpload;
import cn.nyaaar.partridgemngservice.service.file.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

/**
 * 上传文件 Controller
 *
 * @author yuegenhua
 * @Version $Id: UploadService.java, v 0.1 2022-29 10:24 yuegenhua Exp $$
 */
@Tag(name = "upload", description = "上传文件相关 Controller")
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Operation(summary = "检查文件", description = "检查文件分片是否全部上传完成")
    @PostMapping(value = "/check")
    @LogAnnotation
    public R<CheckResp> check(@RequestBody @Validated(FileCheck.class) FileReq fileReq) {
        try {
            return new R<>(uploadService.check(fileReq.getFileName(), fileReq.getFileMd5(), fileReq.getFileBytes(), fileReq.getSourceEnum()));
        } catch (IOException e) {
            log.error("file check error, ", e);
            BusinessExceptionEnum.SYSTEM_ERROR_CUSTOM.assertFail("文件操作异常，请联系管理员");
        }

        return new R<>();
    }

    @Operation(summary = "上传文件分片", description = "上传文件分片")
    @PostMapping(value = "/upload")
    @LogAnnotation
    public R<String> upload(@RequestBody @Validated(FileUpload.class) FileReq fileReq, String shardBase64) {
        try {
            uploadService.upload(fileReq.getShardIndex(), fileReq.getFileMd5(), fileReq.getShardMd5(),
                    Base64.getDecoder().decode(shardBase64), fileReq.getSourceEnum());
        } catch (IOException e) {
            log.error("file check error, ", e);
            BusinessExceptionEnum.SYSTEM_ERROR_CUSTOM.assertFail("文件操作异常，请联系管理员");
        }
        return new R<>();
    }
}
package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.file.FileReq;
import cn.nyaaar.partridgemngservice.model.response.R;
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
    public R<CheckResp> check(@RequestBody Integer eleId) {
        try {
            return new R<>(uploadService.check(eleId));
        } catch (IOException e) {
            log.error("file check error, ", e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();
        }
        return new R<>();
    }

    @Operation(summary = "上传文件分片", description = "上传文件分片")
    @PostMapping(value = "/upload")
    @LogAnnotation
    public R<String> upload(@RequestBody @Validated(FileUpload.class) FileReq fileReq) {
        try {
            uploadService.upload(fileReq.getShardIndex(), fileReq.getFileMd5(), fileReq.getShardMd5(),
                    Base64.getDecoder().decode(fileReq.getShardBase64()));
        } catch (IOException e) {
            log.error("file upload error, ", e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();
        }
        return new R<>();
    }


    @Operation(summary = "上传文件分片", description = "上传文件分片")
    @PostMapping(value = "/delete")
    @LogAnnotation
    public R<String> delete(@RequestBody Integer eleId) {
        try {
            uploadService.delete(eleId);
        } catch (IOException e) {
            log.error("file upload error, ", e);
            BusinessExceptionEnum.FILE_IO_ERROR.assertFail();
        }
        return new R<>();
    }
}
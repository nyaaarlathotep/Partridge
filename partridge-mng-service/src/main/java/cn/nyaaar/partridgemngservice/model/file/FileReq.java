package cn.nyaaar.partridgemngservice.model.file;

import cn.nyaaar.partridgemngservice.model.validate.FileCheck;
import cn.nyaaar.partridgemngservice.model.validate.FileUpload;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 检查文件是否上传完成 Req
 *
 * @author yuegenhua
 * @Version $Id: CheckReq.java, v 0.1 2022-29 10:26 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "文件上传相关 Req")
public class FileReq {

    @Schema(title = "文件名称")
    @NotNull(groups = {FileCheck.class}, message = "请上传文件名称")
    private String fileName;

    @Schema(title = "文件 md5")
    @NotNull(groups = {FileCheck.class, FileUpload.class}, message = "请上传文件 md5")
    private String fileMd5;

    @Schema(title = "文件大小，单位为 Byte")
    @NotNull(groups = {FileCheck.class}, message = "请上传文件大小")
    private Long fileSize;

    @Schema(title = "文件分片序号")
    @NotNull(groups = {FileUpload.class}, message = "请上传文件分片序号")
    private Integer shardIndex;

    @Schema(title = "文件 Base64")
    @NotNull(groups = {FileUpload.class}, message = "请上传文件Base64")
    private String shardBase64;

    @Schema(title = "文件上传者文件系统路径")
    @NotNull(groups = {FileUpload.class}, message = "请上传文件Base64")
    private String uploaderPath;

    @Schema(title = "文件分片 md5")
    @NotNull(groups = {FileUpload.class}, message = "请上传文件分片 md5")
    private String shardMd5;

    @Schema(title = "对应 EleFile 的 Id")
    @NotNull(groups = {FileCheck.class, FileUpload.class}, message = "请上传对应 EleFile 的 Id")
    private Integer EleFileId;
}
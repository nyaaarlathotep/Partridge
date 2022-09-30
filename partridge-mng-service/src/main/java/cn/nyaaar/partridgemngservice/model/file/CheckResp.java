package cn.nyaaar.partridgemngservice.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.Set;

/**
 * 检查文件上传 Resp
 *
 * @author yuegenhua
 * @Version $Id: CheckResp.java, v 0.1 2022-29 10:47 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "检查文件上传 Resp")
public class CheckResp {

    @Schema(title = "缺失分片序号")
    private Set<Integer> missingShardIndex = Collections.emptySet();

    @Schema(title = "分片大小，单位为 Byte")
    private Integer shardSize;

    @Schema(title = "对应文件大小，单位为 Byte")
    private Long size;

    @Schema(title = "对应文件在上传者文件系统的路径")
    private String uploaderPath;

    @Schema(title = "对应 eleFile id")
    private Integer eleFileId;

}
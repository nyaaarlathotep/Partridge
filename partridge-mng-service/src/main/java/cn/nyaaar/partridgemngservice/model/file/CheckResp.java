package cn.nyaaar.partridgemngservice.model.file;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.TreeSet;

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
    Set<Integer> missingShardIndex = new TreeSet<>();

    @Schema(title = "分片大小，单位为 Byte")
    Integer shardSize;
}
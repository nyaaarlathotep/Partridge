package cn.nyaaar.partridgemngservice.model.torrent;

import cn.nyaaar.partridgemngservice.model.validate.Delete;
import cn.nyaaar.partridgemngservice.model.validate.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * 下载优先级 DTO
 *
 * @author yuegenhua
 * @Version $Id: TorrentContentPriorityReq.java, v 0.1 2022-26 14:19 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "Torrent 优先级 DTO")
public class TorrentContentReq {

    @Schema(title = "hash")
    @NotNull(groups = {Priority.class, Delete.class}, message = "请指定 磁链 hash")
    String hash;

    @Schema(title = "integer	File index")
    @NotNull(groups = {Priority.class, Delete.class}, message = "请指定 磁链内容 index")
    Integer index;

    @Schema(title = """
            File priority.
            0\tDo not download
            1\tNormal priority
            6\tHigh priority
            7\tMaximal priority
            """)
    @NotNull(groups = {Priority.class}, message = "请指定 优先级")
    Integer priority;
}
package cn.nyaaar.partridgemngservice.model.torrent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class TorrentContentPriorityReq {

    @Schema(title = "hash")
    String hash;

    @Schema(title = "integer	File index")
    Integer index;

    @Schema(title = """
            File priority.
            0\tDo not download
            1\tNormal priority
            6\tHigh priority
            7\tMaximal priority
            """)
    Integer priority;
}
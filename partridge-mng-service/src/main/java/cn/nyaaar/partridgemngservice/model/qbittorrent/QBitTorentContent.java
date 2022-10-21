package cn.nyaaar.partridgemngservice.model.qbittorrent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: TorrentContent.java, v 0.1 2022-13 14:39 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "qbittorrent Torrent content")
public class QBitTorentContent {

    @Schema(title = "integer	File index")
    private Long index;

    @Schema(title = "File name (including relative path)")
    private String name;

    @Schema(title = "File size (bytes)")
    private Long size;

    @Schema(title = "File progress (percentage/100)")
    private Double progress;

    @Schema(title = "File priority. See possible values here below")
    private Integer priority;

    @Schema(title = "True if file is seeding/complete")
    private Boolean is_seed;

    @Schema(title = "array	The first number is the starting piece index and the second number is the ending piece index (inclusive)")
    private List<Long> piece_range;

    @Schema(title = "Percentage of file pieces currently available (percentage/100)")
    private Double availability;

}
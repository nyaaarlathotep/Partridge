package cn.nyaaar.partridgemngservice.model.qbittorrent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yuegenhua
 * @Version $Id: TorrentDto.java, v 0.1 2022-21 9:51 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "Torrent 信息 DTO")
public class TorrentResp {

    @Schema(title = "Torrent name")
    private String name;

    @Schema(title = "已下载 bytes")
    private Long completed;

    @Schema(title = "Amount of data left to download (bytes)")
    private Long amountLeft;

    @Schema(title = "Total size (bytes) of all file in this torrent (including unselected ones)")
    private Long size;

    @Schema(title = "Torrent progress (percentage/100)")
    private Double progress;

    @Schema(title = "磁链分享比率")
    private Double ratio;

    @Schema(title = "状态")
    private String state;

    @Schema(title = "状态描述")
    private String stateDesc;

    public TorrentResp() {
    }

    public TorrentResp(QBitTorrent qBitTorrent) {
        this.completed = qBitTorrent.getCompleted();
        this.name = qBitTorrent.getName();
        this.size = qBitTorrent.getSize();
        this.amountLeft = qBitTorrent.getAmount_left();
        this.ratio = qBitTorrent.getRatio();
        this.progress = qBitTorrent.getProgress();
        this.state = qBitTorrent.getState().getCode();
        this.stateDesc = qBitTorrent.getState().getDesc();
    }
}
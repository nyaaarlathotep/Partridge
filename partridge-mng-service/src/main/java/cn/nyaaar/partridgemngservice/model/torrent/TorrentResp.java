package cn.nyaaar.partridgemngservice.model.torrent;

import cn.nyaaar.partridgemngservice.entity.EleTorrent;
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

    @Schema(title = "eleId")
    private Long eleId;
    @Schema(title = "Torrent name")
    private String name;

    @Schema(title = "Torrent hash")
    private String hash;

    @Schema(title = "已下载 bytes")
    private Long completed;

    @Schema(title = "Amount of data left to download (bytes)")
    private Long amountLeft;

    @Schema(title = "Total size (bytes) of files selected for download")
    private Long size;

    @Schema(title = "Torrent progress (percentage/100)")
    private Double progress;

    @Schema(title = "磁链分享比率")
    private Double ratio;

    @Schema(title = "状态")
    private String state;

    @Schema(title = "状态描述")
    private String stateDesc;


    public TorrentResp(QBitTorrent qBitTorrent, EleTorrent eleTorrent) {
        this.eleId = eleTorrent.getEleId();
        this.completed = qBitTorrent.getCompleted();
        this.amountLeft = qBitTorrent.getAmount_left();
        this.name = qBitTorrent.getName();
        this.hash = qBitTorrent.getHash();
        this.size = qBitTorrent.getSize();
        this.amountLeft = qBitTorrent.getAmount_left();
        this.ratio = qBitTorrent.getRatio();
        this.progress = qBitTorrent.getProgress();
        this.state = qBitTorrent.getState().getCode();
        this.stateDesc = qBitTorrent.getState().getDesc();
    }
}
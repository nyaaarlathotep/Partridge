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

    @Schema(title = "已下载")
    private Long downloaded;

    @Schema(title = "状态")
    private String state;

    @Schema(title = "状态描述")
    private String stateDesc;

    public TorrentResp() {
    }

    public TorrentResp(QBitTorrent qbittorrent) {
        this.downloaded = qbittorrent.getDownloaded();
        this.state = qbittorrent.getState().getCode();
        this.stateDesc = qbittorrent.getState().getDesc();
    }
}
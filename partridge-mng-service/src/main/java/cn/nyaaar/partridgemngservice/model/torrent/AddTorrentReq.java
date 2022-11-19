package cn.nyaaar.partridgemngservice.model.torrent;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "add Torrent req")
public class AddTorrentReq {

    @Schema(title = "torrent magnet link")
    @NotNull(message = "请指定 torrent magnet link")
    private String magnetLink;
}

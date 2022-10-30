package cn.nyaaar.partridgemngservice.model.jav;

import cn.nyaaar.partridgemngservice.model.torrent.AddTorrentReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "add Torrent req")
public class JavTorrentReq extends AddTorrentReq {

    @Schema(title = "jav code")
    @NotNull(message = "请指定 jav code")
    private String code;
}

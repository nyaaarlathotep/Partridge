package cn.nyaaar.partridgemngservice.model.qbittorrent;

import cn.nyaaar.partridgemngservice.common.enums.QbittorrentStateEnum;
import cn.nyaaar.partridgemngservice.entity.EleTorrent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author yuegenhua
 * @Version $Id: Torrent.java, v 0.1 2022-10 16:30 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "qbittorrent Torrent downloading info")
public class QBitTorrent {

    @Schema(title = "Time (Unix Epoch) when the torrent was added to the client")
    private Long added_on;

    @Schema(title = "Amount of data left to download (bytes)")
    private Long amount_left;

    @Schema(title = "Whether this torrent is managed by Automatic Torrent Management")
    private Boolean auto_tmm;

    @Schema(title = "Percentage of file pieces currently available")
    private Double availability;

    @Schema(title = "Category of the torrent")
    private String category;

    @Schema(title = "Amount of transfer data completed (bytes)")
    private Long completed;

    @Schema(title = "Time (Unix Epoch) when the torrent completed")
    private Long completion_on;

    @Schema(title = "Absolute path of torrent content (root path for multiple torrents, absolute file path for single file torrents)")
    private String content_path;

    @Schema(title = "Torrent download speed limit (bytes/s). -1 if unlimited.")
    private Long dl_limit;

    @Schema(title = "Torrent download speed (bytes/s)")
    private Long dlspeed;

    @Schema(title = "Amount of data downloaded")
    private Long downloaded;

    @Schema(title = "Amount of data downloaded this session")
    private Long downloaded_session;

    @Schema(title = "Torrent ETA (seconds)")
    private Long eta;

    @Schema(title = "True if first last piece are prioritized")
    private Boolean f_l_piece_prio;

    @Schema(title = "True if force start is enabled for this torrent")
    private Boolean force_start;

    @Schema(title = "Torrent hash")
    private String hash;

    @Schema(title = "Last time (Unix Epoch) when a chunk was downloaded/uploaded")
    private Long last_activity;

    @Schema(title = "Magnet URI corresponding to this torrent")
    private String magnet_uri;

    @Schema(title = "Maximum share ratio until torrent is stopped from seeding/uploading")
    private Double max_ratio;

    @Schema(title = "Maximum seeding time (seconds) until torrent is stopped from seeding")
    private Long max_seeding_time;

    @Schema(title = "Torrent name")
    private String name;

    @Schema(title = "Number of seeds in the swarm")
    private Long num_complete;

    @Schema(title = "Number of leechers in the swarm")
    private Long num_incomplete;

    @Schema(title = "Number of leechers connected to")
    private Long num_leechs;

    @Schema(title = "Number of seeds connected to")
    private Long num_seeds;

    @Schema(title = "Torrent priority. Returns -1 if queuing is disabled or torrent is in seed mode")
    private Long priority;

    @Schema(title = "Torrent progress (percentage/100)")
    private Double progress;

    @Schema(title = "Torrent share ratio. Max ratio value: 9999.")
    private Double ratio;

    @Schema(title = "TODO (what is different from max_ratio?)")
    private Double ratio_limit;

    @Schema(title = "Path where this torrent's data is stored")
    private String save_path;

    @Schema(title = "Torrent elapsed time while complete (seconds)")
    private Long seeding_time;

    @Schema(title = "TODO (what is different from max_seeding_time?) seeding_time_limit is a per torrent setting, when Automatic Torrent Management is disabled, furthermore then max_seeding_time is set to seeding_time_limit for this torrent. If Automatic Torrent Management is enabled, the value is -2. And if max_seeding_time is unset it have a default value -1.")
    private Long seeding_time_limit;

    @Schema(title = "Time (Unix Epoch) when this torrent was last seen complete")
    private Long seen_complete;

    @Schema(title = "True if sequential download is enabled")
    private Boolean seq_dl;

    @Schema(title = "Total size (bytes) of files selected for download")
    private Long size;

    @Schema(title = "Torrent state. See table here below for the possible values")
    private QbittorrentStateEnum state;

    @Schema(title = "True if super seeding is enabled")
    private Boolean super_seeding;

    @Schema(title = "Comma-concatenated tag list of the torrent")
    private String tags;

    @Schema(title = "Total active time (seconds)")
    private Long time_active;

    @Schema(title = "Total size (bytes) of all file in this torrent (including unselected ones)")
    private Long total_size;

    @Schema(title = "The first tracker with working status. Returns empty String if no tracker is working.")
    private String tracker;

    @Schema(title = "Torrent upload speed limit (bytes/s). -1 if unlimited.")
    private Long up_limit;

    @Schema(title = "Amount of data uploaded")
    private Long uploaded;

    @Schema(title = "Amount of data uploaded this session")
    private Long uploaded_session;

    @Schema(title = "Time (Unix Epoch) when the torrent was added to the client")
    private Long upspeed;

    public boolean equalElementTorrent(EleTorrent eleTorrent) {
        return Objects.equals(this.getName(), eleTorrent.getName())
                && Objects.equals(this.getSize(), eleTorrent.getSize())
                && Objects.equals(this.getState().toString(), eleTorrent.getState());

    }

}
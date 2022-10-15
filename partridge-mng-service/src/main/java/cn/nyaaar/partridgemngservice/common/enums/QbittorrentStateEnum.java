package cn.nyaaar.partridgemngservice.common.enums;

import lombok.Getter;

/**
 * @author yuegenhua
 * @Version $Id: QbittorrentState.java, v 0.1 2022-10 16:20 yuegenhua Exp $$
 */
@Getter
public enum QbittorrentStateEnum {
    error("error", "Some error occurred, applies to paused torrents"),
    missingFiles("missingFiles", "Torrent data files is missing"),
    uploading("uploading", "Torrent is being seeded and data is being transferred"),
    pausedUP("pausedUP", "Torrent is paused and has finished downloading"),
    queuedUP("queuedUP", "Queuing is enabled and torrent is queued for upload"),
    stalledUP("stalledUP", "Torrent is being seeded, but no connection were made"),
    checkingUP("checkingUP", "Torrent has finished downloading and is being checked"),
    forcedUP("forcedUP", "Torrent is forced to uploading and ignore queue limit"),
    allocating("allocating", "Torrent is allocating disk space for download"),
    downloading("downloading", "Torrent is being downloaded and data is being transferred"),
    metaDL("metaDL", "Torrent has just started downloading and is fetching metadata"),
    pausedDL("pausedDL", "Torrent is paused and has NOT finished downloading"),
    queuedDL("queuedDL", "Queuing is enabled and torrent is queued for download"),
    stalledDL("stalledDL", "Torrent is being downloaded, but no connection were made"),
    checkingDL("checkingDL", "Same as checkingUP, but torrent has NOT finished downloading"),
    forcedDL("forcedDL", "Torrent is forced to downloading to ignore queue limit"),
    checkingResumeData("checkingResumeData", "Checking resume data on qBt startup"),
    moving("moving", "Torrent is moving to another location"),
    unknown("unknown", "Unknown status"),
    ;

    private final String code;
    private final String desc;

    QbittorrentStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
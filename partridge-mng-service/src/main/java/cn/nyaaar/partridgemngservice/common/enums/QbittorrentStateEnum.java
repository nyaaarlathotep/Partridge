package cn.nyaaar.partridgemngservice.common.enums;

import lombok.Getter;

/**
 * @author yuegenhua
 * @Version $Id: QbittorrentState.java, v 0.1 2022-10 16:20 yuegenhua Exp $$
 */
@Getter
public enum QbittorrentStateEnum {
    error("error", false, "Some error occurred, applies to paused torrents"),
    missingFiles("missingFiles", false, "Torrent data files is missing"),
    uploading("uploading", true, "Torrent is being seeded and data is being transferred"),
    pausedUP("pausedUP", true, "Torrent is paused and has finished downloading"),
    queuedUP("queuedUP", true, "Queuing is enabled and torrent is queued for upload"),
    stalledUP("stalledUP", true, "Torrent is being seeded, but no connection were made"),
    checkingUP("checkingUP", true, "Torrent has finished downloading and is being checked"),
    forcedUP("forcedUP", true, "Torrent is forced to uploading and ignore queue limit"),
    allocating("allocating", false, "Torrent is allocating disk space for download"),
    downloading("downloading", false, "Torrent is being downloaded and data is being transferred"),
    metaDL("metaDL", false, "Torrent has just started downloading and is fetching metadata"),
    pausedDL("pausedDL", false, "Torrent is paused and has NOT finished downloading"),
    queuedDL("queuedDL", false, "Queuing is enabled and torrent is queued for download"),
    stalledDL("stalledDL", false, "Torrent is being downloaded, but no connection were made"),
    checkingDL("checkingDL", false, "Same as checkingUP, but torrent has NOT finished downloading"),
    forcedDL("forcedDL", false, "Torrent is forced to downloading to ignore queue limit"),
    checkingResumeData("checkingResumeData", false, "Checking resume data on qBt startup"),
    moving("moving", false, "Torrent is moving to another location"),
    unknown("unknown", false, "Unknown status"),
    ;

    private final String code;
    private final Boolean finished;
    private final String desc;

    QbittorrentStateEnum(String code, Boolean finished, String desc) {
        this.code = code;
        this.finished = finished;
        this.desc = desc;
    }
}
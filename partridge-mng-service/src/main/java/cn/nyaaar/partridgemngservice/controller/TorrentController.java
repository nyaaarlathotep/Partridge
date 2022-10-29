package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.model.torrent.QBitTorrentContent;
import cn.nyaaar.partridgemngservice.model.torrent.TorrentContentReq;
import cn.nyaaar.partridgemngservice.model.torrent.TorrentResp;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.validate.Delete;
import cn.nyaaar.partridgemngservice.model.validate.Priority;
import cn.nyaaar.partridgemngservice.service.torrent.TorrentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: TorrentController.java, v 0.1 2022-26 13:24 yuegenhua Exp $$
 */
@Tag(name = "torrent", description = "torrent 相关 Controller")
@RestController
@RequestMapping("/torrent")
@Slf4j
public class TorrentController {

    private final TorrentService torrentService;

    public TorrentController(TorrentService torrentService) {
        this.torrentService = torrentService;
    }

    @Operation(summary = "获取正在下载的 torrent", description = "获取当前用户创建的正在下载的 torrent")
    @GetMapping(value = "/downloading")
    @LogAnnotation
    public R<List<TorrentResp>> getDownloadingTorrents() {
        return new R<>(torrentService.getDownloadingTorrents());
    }


    @Operation(summary = "获取所有 torrent", description = "获取当前用户创建的的 torrent")
    @GetMapping(value = "/all")
    @LogAnnotation
    public R<List<TorrentResp>> getAllTorrents() {

        return new R<>(torrentService.getTorrents());
    }


    @Operation(summary = "获取 hash 对应 torrent 的内容",
            description = "获取 hash 对应 torrent 的内容，如果 torrent 元数据未下载完成，不会显示")
    @GetMapping(value = "/content")
    @LogAnnotation
    public R<List<QBitTorrentContent>> getTorrentContent(@RequestParam String hash) {

        return new R<>(torrentService.getTorrentContent(hash));
    }


    @Operation(summary = "设置 torrent content 下载优先级", description = "设置 torrent content 的下载优先级")
    @PostMapping(value = "/priority")
    @LogAnnotation
    public R<String> setContentPriority(@RequestBody @Validated(Priority.class) TorrentContentReq torrentContentReq) {
        torrentService.setContentPriority(torrentContentReq.getHash(), torrentContentReq.getIndex(), torrentContentReq.getPriority());
        return new R<>();
    }


    @Operation(summary = "删除 torrent 的内容", description = "删除 torrent 的内容")
    @GetMapping(value = "/delete/content")
    @LogAnnotation
    public R<String> deleteTorrentContent(@RequestParam @Validated(Delete.class) TorrentContentReq torrentContentReq) {
        torrentService.deleteTorrentContent(torrentContentReq.getHash(), torrentContentReq.getIndex());
        return new R<>();
    }

    @Operation(summary = "删除 torrent", description = "删除 torrent")
    @GetMapping(value = "/delete/torrent")
    @LogAnnotation
    public R<String> deleteTorrent(@RequestParam String hash) {
        torrentService.deleteTorrent(hash);
        return new R<>();
    }

    @Operation(summary = "torrent 下载完成回调", description = "torrent 下载完成回调")
    @GetMapping(value = "/callback")
    @LogAnnotation
    public R<String> downloadCompleteCallback(@RequestParam String hash) {
        torrentService.callBack(hash);
        return new R<>();
    }

}
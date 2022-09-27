package cn.nyaaar.partridgemngservice.model.eh;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.common.constants.Settings;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 下载中的 gallery
 *
 * @author nyaaar
 * @Version $Id: DownloadingGallery.java, v 0.1 2022-16 14:39 nyaaar Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "下载中的 gallery")
public class DownloadingGallery {

    private long gid;

    private String gtoken;

    @Schema(title = "画廊标题")
    private String title;

    private long eleId;

    @Schema(title = "画廊页数")
    private int pages;

    private String folderPath;

    private String userName;

    @Schema(title = "画廊下载最后期限")
    private Date deadline = DateUtil.date().offset(DateField.MINUTE, Settings.getDownloadQueueExpireTime());

    @Schema(title = "画廊下载成功页数")
    private AtomicInteger downloadCompleteNum = new AtomicInteger();

    @Schema(title = "画廊下载失败页数与失败次数")
    private Map<Integer, Integer> downloadFailPageIndex = new HashMap<>();
}
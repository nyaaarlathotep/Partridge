package cn.nyaaar.partridgemngservice.model.eh;

import cn.nyaaar.partridgemngservice.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: GalleryBasicInfo.java, v 0.1 2022-05 14:39 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class GalleryBasicInfo {

    @Schema(title = "ehentai gallery id")
    private Long gid;

    private Long eleId;

    private String title;

    private String titleJpn;

    @Schema(title = "tag")
    private List<Tag> tags;

    @Schema(title = "ehentai gallery id")
    private Integer category;

    @Schema(title = "上传者")
    private String uploader;

    @Schema(title = "评分")
    private String rating;

    @Schema(title = "评分人数")
    private Integer ratingCount;

    @Schema(title = "总页数")
    private Integer pages;

    @Schema(title = "预览画廊对应页")
    private Integer previewPage;

    @Schema(title = "gtoken")
    private String token;

    @Schema(title = "上传时间")
    private Date posted;

    @Schema(title = "喜爱数")
    private Integer favoriteCount;

    @Schema(title = "(0-否;1-是)")
    private Integer cashedFlag;

    @Schema(title = "(0-否;1-是)")
    private Integer downloadFlag;

    private Date createdTime;

    private Date updatedTime;

}
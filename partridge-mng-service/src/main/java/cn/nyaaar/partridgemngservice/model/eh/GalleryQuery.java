package cn.nyaaar.partridgemngservice.model.eh;

import cn.nyaaar.partridgemngservice.model.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * gallery 查询 DTO 类
 *
 * @author yuegenhua
 * @Version $Id: GalleryQuery.java, v 0.1 2022-13 10:34 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "gallery 查询 DTO 类")
public class GalleryQuery {

    @Schema(title = "ehentai id")
    private Long gid;

    private Long eleId;

    @Schema(title = "模糊搜索 title")
    private String title;

    @Schema(title = "模糊搜索 titleJpn")
    private String titleJpn;

    @Schema(title = "tag")
    private List<TagDto> tagDtos;

    @Schema(title = "ehentai category")
    private Integer category;

    @Schema(title = "模糊搜索 上传者")
    private String uploader;

    @Schema(title = "评分上限")
    private Double ratingCeiling;

    @Schema(title = "评分下限")
    private Double ratingFloor;

    @Schema(title = "评分人数上限")
    private Integer ratingCountCeiling;

    @Schema(title = "评分人数下限")
    private Integer ratingCountFloor;

    @Schema(title = "总页数上限")
    private Integer pagesCeiling;

    @Schema(title = "总页数下限")
    private Integer pagesFloor;

    @Schema(title = "上传时间起始")
    private Date postedStart;

    @Schema(title = "上传时间结束")
    private Date postedEnd;

    @Schema(title = "喜爱数上限")
    private Integer favoriteCountCeiling;

    @Schema(title = "喜爱数下限")
    private Integer favoriteCountFloor;

    @Schema(title = "根据 gid 排序(默认)")
    private Boolean orderByGid;

    @Schema(title = "根据 eleId 排序")
    private Boolean orderByEleID;

    @Schema(title = "(0-否;1-是)")
    private Integer cashedFlag;

    @Schema(title = "(0-否;1-是)")
    private Integer downloadFlag;
    

}
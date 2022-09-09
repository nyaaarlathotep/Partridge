package cn.nyaaar.partridgemngservice.model.jav;

import cn.nyaaar.partridgemngservice.model.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "jav 基本信息")
public class JavBasicInfo {

    @Schema(title = "ELE_ID")
    private Integer eleId;

    @Schema(title = "識別碼")
    private String code;

    @Schema(title = "TITLE")
    private String title;

    @Schema(title = "發行日期")
    private Date publishDate;

    @Schema(title = "長度")
    private String length;

    @Schema(title = "导演")
    private String director;

    @Schema(title = "系列")
    private String series;

    @Schema(title = "演员")
    private List<String> actors;

    @Schema(title = "发行商")
    private String publisher;

    @Schema(title = "制作商")
    private String producer;

    @Schema(title = "tag")
    private List<TagDto> tags;
}

package cn.nyaaar.partridgemngservice.model.jav;

import cn.nyaaar.partridgemngservice.model.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author nyaaar
 * @Version $Id: JavQuery.java, v 0.1 2022-15 15:16 nyaaar Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "jav 查询 DTO 类")
public class JavQuery {
    @Schema(title = "ELE_ID")
    private Integer eleId;

    @Schema(title = "識別碼")
    private String code;

    @Schema(title = "模糊搜索 TITLE")
    private String title;

    @Schema(title = "發行日期起始")
    private Date publishDateStart;

    @Schema(title = "發行日期结束")
    private Date publishDateEnd;

    @Schema(title = "长度上限")
    private String lengthCeiling;

    @Schema(title = "长度下限")
    private String lengthFloor;

    @Schema(title = "导演")
    private String director;

    @Schema(title = "系列")
    private String series;

    @Schema(title = "演员")
    private List<String> actors;

    @Schema(title = "相关组织")
    private List<String> organizations;

    @Schema(title = "tag")
    private List<TagDto> tagDtos;
    // TODO desc by...
}
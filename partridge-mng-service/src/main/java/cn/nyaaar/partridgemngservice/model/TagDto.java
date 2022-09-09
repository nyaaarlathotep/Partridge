package cn.nyaaar.partridgemngservice.model;

import cn.nyaaar.partridgemngservice.common.enums.SourceEnum;
import cn.nyaaar.partridgemngservice.entity.TagInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "tag 基本信息")
public class TagDto {

    @Schema(title = "tag id")
    private Integer id;

    @Schema(title = "tag 名称")
    private String name;

    @Schema(title = "tag 所属大类")
    private String groupName;

    @Schema(title = "tag 来源")
    private String source;

    public TagDto(TagInfo tagInfo) {
        this.id = tagInfo.getId();
        this.name = tagInfo.getName();
        this.groupName = tagInfo.getGroupName();
        this.source = Objects.requireNonNullElse
                (SourceEnum.fromCode(tagInfo.getSource()), SourceEnum.Unknown).getDesc();

    }
}

package cn.nyaaar.partridgemngservice.model.element;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author nyaaar
 * @Version $Id: ElementDto.java, v 0.1 2022-22 15:50 nyaaar Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "ElementDto")
public class ElementDto {
    @Schema(title = "主键id")
    private Long id;

    @Schema(title = "element类型")
    private String type;

    @Schema(title = "收到喜爱的数量")
    private Integer likes;

    @Schema(title = "是否可用，已被上传者删除或其他原因")
    private Boolean available;

    @Schema(title = "是否完成")
    private Boolean completed;

}
package cn.nyaaar.partridgemngservice.model.element;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * CollectionEleDto
 *
 * @author yuegenhua
 * @Version $Id: CollectionEleDto.java, v 0.1 2022-31 17:34 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "CollectionEleDto")
public class CollectionEleDto {

    @Schema(title = "合集主键 id")
    @NotNull(message = "请指定合集主键 id")
    private Integer collectionId;

    @Schema(title = "元素主键 Id")
    @NotNull(message = "请指定元素主键 Id")
    private Long eleId;
}
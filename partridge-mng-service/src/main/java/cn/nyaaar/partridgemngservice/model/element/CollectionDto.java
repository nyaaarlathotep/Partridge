package cn.nyaaar.partridgemngservice.model.element;

import cn.nyaaar.partridgemngservice.model.validate.Add;
import cn.nyaaar.partridgemngservice.model.validate.Delete;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 合集 DTO
 *
 * @author yuegenhua
 * @Version $Id: CollectionDto.java, v 0.1 2022-31 16:57 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "CollectionDto")
public class CollectionDto {
    @Schema(title = "主键 id")
    @NotNull(groups = {Delete.class}, message = "请指定主键 id")
    private Integer id;

    @Schema(title = "合集包含的 element 主键 id")
    private List<Long> eleIds;

    @Schema(title = "合集名称")
    @NotNull(groups = {Add.class}, message = "请指定合集名称")
    private String name;

    @Schema(title = "合集描述")
    private String desc;

    @Schema(title = "创建时间")
    private Date createdTime;

    @Schema(title = "收到喜爱的数量")
    private Integer likes;
}
/**
 * Saicfinance.com Inc.
 * Copyright (c) 1994-2022 All Rights Reserved.
 */
package cn.nyaaar.partridgemngservice.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author yuegenhua
 * @Version $Id: ElementDto.java, v 0.1 2022-22 15:50 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "ElementDto")
public class ElementDto {
    @Schema(name = "主键id")
    private Integer id;

    @Schema(name = "element类型")
    private String type;

}
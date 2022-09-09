package cn.nyaaar.partridgemngservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EleOrgRe extends Model<EleOrgRe> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("ELE_ID")
    private Long eleId;

    @TableField("ORG_ID")
    private Integer orgId;

    @TableField("RE_TYPE")
    private String reType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

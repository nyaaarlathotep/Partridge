package cn.nyaaar.partridgemngservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合集表
 * </p>
 *
 * @author nyaaar
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PrCollection extends Model<PrCollection> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("USER_NAME")
    private String userName;

    /**
     * 合集名称
     */
    @TableField("C_NAME")
    private String cName;

    /**
     * 合集描述
     */
    @TableField("C_DESC")
    private String cDesc;

    /**
     * 分享标志(0-否;1-是)
     */
    @TableField("SHARED_FLAG")
    private Integer sharedFlag;

    /**
     * 启用标志(0-禁用;1-启用)
     */
    @TableField("AVAILABLE_FLAG")
    private Integer availableFlag;

    /**
     * 创建时间
     */
    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * 修改时间
     */
    @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

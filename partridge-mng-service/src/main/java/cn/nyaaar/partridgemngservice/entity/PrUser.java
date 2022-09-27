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
 * 用户表
 * </p>
 *
 * @author nyaaar
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PrUser extends Model<PrUser> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("EMAIL")
    private String email;

    @TableField("USER_NAME")
    private String userName;

    @TableField("PASSWORD")
    private String password;

    /**
     * (0-否;1-是) 是否为有效账户
     */
    @TableField("VALIDATED")
    private Integer validated;

    /**
     * 上次登陆时间
     */
    @TableField("LAST_LOGIN_TIME")
    private Date lastLoginTime;

    /**
     * 用户空间配额
     */
    @TableField("SPACE_QUOTA")
    private Long spaceQuota;

    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private Date createdTime;

    @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;

    /**
     * 上次登陆ip
     */
    @TableField("LAST_LOGIN_IP")
    private String lastLoginIp;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

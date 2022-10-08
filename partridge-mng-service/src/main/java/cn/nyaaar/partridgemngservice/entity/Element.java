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
 * 基本元素表
 * </p>
 *
 * @author nyaaar
 * @since 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Element extends Model<Element> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField("TYPE")
    private String type;

    /**
     * 关联文件所在目录
     */
    @TableField("FILE_PATH")
    private String filePath;

    /**
     * 关联文件总大小，单位为 B
     */
    @TableField("FILE_SIZE")
    private Long fileSize;

    /**
     * 分享标志(0-否;1-是)
     */
    @TableField("SHARED_FLAG")
    private Integer sharedFlag;

    /**
     * 释放标志(0-否;1-是)，释放后上传者将不能删除元素
     */
    @TableField("FREED_FLAG")
    private Integer freedFlag;

    /**
     * 上传用户
     */
    @TableField("UPLOADER")
    private String uploader;

    /**
     * 启用标志(0-禁用;1-启用)
     */
    @TableField("AVAILABLE_FLAG")
    private Integer availableFlag;

    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private Date createdTime;

    @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

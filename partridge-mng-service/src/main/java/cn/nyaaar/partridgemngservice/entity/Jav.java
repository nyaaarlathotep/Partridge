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
 *
 * </p>
 *
 * @author nyaaar
 * @since 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Jav extends Model<Jav> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "ELE_ID", type = IdType.AUTO)
    private Integer eleId;

    /**
     * 識別碼
     */
    @TableField("CODE")
    private String code;

    @TableField("TITLE")
    private String title;

    /**
     * 發行日期
     */
    @TableField("PUBLISH_DATE")
    private Date publishDate;

    /**
     * 長度
     */
    @TableField("LENGTH")
    private String length;

    @TableField("DIRECTOR")
    private String director;

    @TableField("SERIES")
    private String series;

    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private Date createdTime;

    @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.eleId;
    }

}

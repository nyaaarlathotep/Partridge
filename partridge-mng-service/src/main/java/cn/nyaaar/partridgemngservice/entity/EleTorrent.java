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
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EleTorrent extends Model<EleTorrent> {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("ELE_ID")
    private Long eleId;

    @TableField("NAME")
    private String name;

    /**
     * Torrent hash
     */
    @TableField("HASH")
    private String hash;

    /**
     * Torrent state
     */
    @TableField("STATE")
    private String state;

    /**
     * Total size (bytes) of files selected for download
     */
    @TableField("SIZE")
    private Long size;

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

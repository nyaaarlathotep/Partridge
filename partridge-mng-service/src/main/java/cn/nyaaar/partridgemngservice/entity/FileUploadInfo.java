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
 * @since 2022-09-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FileUploadInfo extends Model<FileUploadInfo> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 对应 ELE_FILE 的 id
     */
    @TableField("ELE_FILE_ID")
    private Long eleFileId;

    /**
     * 绝对路径
     */
    @TableField("PATH")
    private String path;

    /**
     * 文件在上传者文件系统的路径
     */
    @TableField("UPLOADER_PATH")
    private String uploaderPath;

    /**
     * 文件名
     */
    @TableField("NAME")
    private String name;

    /**
     * 文件后缀
     */
    @TableField("SUFFIX")
    private String suffix;

    /**
     * 文件大小(字节B)
     */
    @TableField("SIZE")
    private Long size;

    /**
     * 已经上传的分片
     */
    @TableField("SHARD_NUM")
    private Integer shardNum;

    /**
     * 分片大小(字节B)
     */
    @TableField("SHARD_SIZE")
    private Integer shardSize;

    /**
     * 分片总数
     */
    @TableField("SHARD_TOTAL")
    private Integer shardTotal;

    /**
     * 文件标识
     */
    @TableField("FILE_KEY")
    private String fileKey;

    /**
     * 上传是否完成标志位，(0-未完成;1-完成)
     */
    @TableField("UPLOAD_FLAG")
    private Integer uploadFlag;

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

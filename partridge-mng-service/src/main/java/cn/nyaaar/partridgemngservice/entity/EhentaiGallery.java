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
 * @since 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EhentaiGallery extends Model<EhentaiGallery> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ehentai gallery id
     */
    @TableId(value = "GID", type = IdType.ASSIGN_ID)
    private Long gid;

    @TableField("ELE_ID")
    private Long eleId;

    @TableField("TITLE")
    private String title;

    @TableField("TITLE_JPN")
    private String titleJpn;

    /**
     * gallery分类
     */
    @TableField("CATEGORY")
    private Integer category;

    /**
     * 上传者
     */
    @TableField("UPLOADER")
    private String uploader;

    /**
     * 评分
     */
    @TableField("RATING")
    private String rating;

    /**
     * 评分人数
     */
    @TableField("RATING_COUNT")
    private Integer ratingCount;

    /**
     * 总页数
     */
    @TableField("PAGES")
    private Integer pages;

    /**
     * 预览画廊对应页
     */
    @TableField("PREVIEW_PAGE")
    private Integer previewPage;

    /**
     * gtoken
     */
    @TableField("TOKEN")
    private String token;

    /**
     * 上传时间
     */
    @TableField("POSTED")
    private Date posted;

    /**
     * 喜爱数
     */
    @TableField("FAVORITE_COUNT")
    private Integer favoriteCount;

    /**
     * (0-否;1-是)
     */
    @TableField("CASHED_FLAG")
    private Integer cashedFlag;

    /**
     * (0-否;1-是)
     */
    @TableField("DOWNLOAD_FLAG")
    private Integer downloadFlag;

    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private Date createdTime;

    @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;


    @Override
    protected Serializable pkVal() {
        return this.gid;
    }

}

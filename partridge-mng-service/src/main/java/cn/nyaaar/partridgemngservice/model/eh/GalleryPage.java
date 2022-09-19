package cn.nyaaar.partridgemngservice.model.eh;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 画廊单页DTO类
 *
 * @author nyaaar
 * @Version $Id: GalleryPage.java, v 0.1 2022-08 9:04 nyaaar Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "画廊单页DTO类")
public class GalleryPage {

    @Schema(title = "图片base64")
    String pageBase64;

    @Schema(title = "图片页码，逻辑页码")
    Integer pageIndex;

    @Schema(title = "文件类型后缀，如 .jpg")
    String fileSuffix;
}
package cn.nyaaar.partridgemngservice.model.eh;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 预览画廊响应
 *
 * @author nyaaar
 * @Version $Id: EhPreviewResp.java, v 0.1 2022-07 17:08 nyaaar Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "预览画廊响应")
public class EhViewResp {

    @Schema(title = "画廊单页")
    List<GalleryPage> pages;

}
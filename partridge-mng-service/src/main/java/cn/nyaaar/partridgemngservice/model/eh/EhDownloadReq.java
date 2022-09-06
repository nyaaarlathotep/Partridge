package cn.nyaaar.partridgemngservice.model.eh;

import cn.nyaaar.partridgemngservice.model.validate.EhDownload;
import cn.nyaaar.partridgemngservice.model.validate.EhPreview;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yuegenhua
 * @Version $Id: EhDownloadReq.java, v 0.1 2022-05 17:38 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EhDownloadReq {

    @Schema(title = "gid")
    @NotNull(groups = {EhDownload.class, EhPreview.class}, message = "请指定eh画廊ID gid")
    private Long gid;

    @Schema(title = "gtoken")
    @NotNull(groups = {EhDownload.class, EhPreview.class}, message = "请指定eh画廊token gtoken")
    private Long gtoken;

    @Schema(title = "预览页码")
    @NotNull(groups = {EhPreview.class}, message = "请指定eh画廊 预览页码")
    private List<Integer> pageIndexes;
}
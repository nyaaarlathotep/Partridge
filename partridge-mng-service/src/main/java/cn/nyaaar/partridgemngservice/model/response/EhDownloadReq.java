package cn.nyaaar.partridgemngservice.model.response;

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

    @Schema(name = "gid")
    private Long gid;

    @Schema(name = "gtoken")
    private Long gtoken;

    @Schema(name = "预览页码")
    private List<Integer> pageIndexes;
}
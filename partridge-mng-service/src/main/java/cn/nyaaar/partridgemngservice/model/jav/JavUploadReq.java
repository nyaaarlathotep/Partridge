package cn.nyaaar.partridgemngservice.model.jav;

import cn.nyaaar.partridgemngservice.model.file.FileReq;
import cn.nyaaar.partridgemngservice.model.validate.FileCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author yuegenhua
 * @Version $Id: JavUploadReq.java, v 0.1 2022-29 17:48 yuegenhua Exp $$
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Tag(name = "jav 上传 DTO 类")
public class JavUploadReq extends FileReq {
   
    @Schema(title = "jav code")
    @NotNull(groups = {FileCheck.class}, message = "请上传对应的 jav code")
    private String code;
}
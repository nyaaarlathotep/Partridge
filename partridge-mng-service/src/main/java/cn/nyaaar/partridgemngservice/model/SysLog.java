package cn.nyaaar.partridgemngservice.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysLog implements Serializable {

    private String id;

    @Schema(title = "操作")
    private String operation;

    @Schema(title = "调用方法")
    private String method;

    @Schema(title = "参数")
    private String params;

    @Schema(title = "调用时长")
    private Long during;

    @Schema(title = "IP")
    private String ip;

    @Schema(title = "返回结果")
    private String result;

}
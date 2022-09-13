package cn.nyaaar.partridgemngservice.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysLog implements Serializable {

    private String id;

    @Schema(name = "操作")
    private String operation;

    @Schema(name = "调用方法")
    private String method;

    @Schema(name = "参数")
    private String params;

    @Schema(name = "调用时长")
    private Long during;

    @Schema(name = "IP")
    private String ip;

    @Schema(name = "返回结果")
    private String result;

}
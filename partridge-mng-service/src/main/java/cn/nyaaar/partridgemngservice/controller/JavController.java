package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.jav.ListResp;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.service.jav.JavMngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "jav", description = "jav 相关 Controller")
@RestController
@RequestMapping("/jav")
@Slf4j
public class JavController {
    private final JavMngService javMngService;

    public JavController(JavMngService javMngService) {
        this.javMngService = javMngService;
    }

    @Operation(summary = "jav 基本信息", description = "通过 code 获取 jav 基本信息")
    @GetMapping(value = "/code/{code}")
    public R<JavBasicInfo> getJavBasicInfoById(@PathVariable String code) {

        return new R<>(javMngService.getJavBasicInfoByCode(code));
    }

    @Operation(summary = "jav 基本信息列表", description = "通过 name 模糊搜索获取 jav 基本信息")
    @GetMapping(value = "/name/{name}/{pageIndex}")
    public R<ListResp<JavBasicInfo>> getJavBasicInfoList(@PathVariable String name, @PathVariable Integer pageIndex) {

        return new R<>(javMngService.getJavList(name, pageIndex));
    }

    @Operation(summary = "jav 基本信息列表", description = "获取保存在数据库的 jav 基本信息列表")
    @GetMapping(value = "/list/{pageIndex}")
    public R<ListResp<JavBasicInfo>> getJavBasicInfoList(@PathVariable Integer pageIndex) {

        return new R<>(javMngService.getJavList(pageIndex));
    }

}

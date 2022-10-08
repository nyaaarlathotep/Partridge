package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.model.file.CheckResp;
import cn.nyaaar.partridgemngservice.model.jav.JavBasicInfo;
import cn.nyaaar.partridgemngservice.model.ListResp;
import cn.nyaaar.partridgemngservice.model.jav.JavQuery;
import cn.nyaaar.partridgemngservice.model.jav.JavUploadReq;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.validate.FileCheck;
import cn.nyaaar.partridgemngservice.service.jav.JavMngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "jav", description = "jav 相关 Controller")
@RestController
@RequestMapping("/jav")
@Slf4j
public class JavController {
    // TODO refactor to special video
    private final JavMngService javMngService;

    public JavController(JavMngService javMngService) {
        this.javMngService = javMngService;
    }

    @Operation(summary = "jav 基本信息", description = "通过 code 获取 jav 基本信息")
    @GetMapping(value = "/code")
    @LogAnnotation
    public R<JavBasicInfo> getJavBasicInfoById(@RequestParam String code) {

        return new R<>(javMngService.getJavBasicInfoByCode(code));
    }

    @Operation(summary = "jav 基本信息列表", description = "通过高级搜索获取 jav 基本信息，不存在的演员，机构等信息不会加入搜索条件")
    @PostMapping(value = "/search/{pageIndex}")
    @LogAnnotation
    public R<ListResp<JavBasicInfo>> getJavBasicInfoList(@RequestBody JavQuery javQuery, @PathVariable Integer pageIndex) {

        return new R<>(javMngService.getJavList(javQuery, pageIndex));
    }


    @Operation(summary = "上传 Jav", description = "上传 Jav，返回 check 的结果")
    @PostMapping(value = "/upload")
    @LogAnnotation
    public R<CheckResp> uploadJav(@RequestBody @Validated(FileCheck.class) JavUploadReq javUploadReq) {

        return new R<>(javMngService.uploadJav(javUploadReq));
    }

    @Operation(summary = "获取未上传完成的 Jav", description = "返回 check 的结果列表")
    @GetMapping(value = "/uploading")
    @LogAnnotation
    public R<List<CheckResp>> getUploadingJavs() {

        return new R<>(javMngService.getUploadingJavs());
    }

}

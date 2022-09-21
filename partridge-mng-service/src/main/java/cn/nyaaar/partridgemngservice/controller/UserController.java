package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.user.RegistrationRequest;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import cn.nyaaar.partridgemngservice.service.user.impl.AppUserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user", description = "user 相关 Controller")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Operation(summary = "注册", description = "注册")
    @PostMapping(value = "/register")
    @LogAnnotation
    public R<String> register(@RequestBody RegistrationRequest request) {

        return new R<>(appUserService.register(request));
    }

    @Operation(summary = "验证 token", description = "验证 token")
    @GetMapping(value = "/confirm")
    @LogAnnotation
    public R<Boolean> confirm(@RequestParam("token") String token) {
        return new R<>(appUserService.confirmToken(token));
    }


    @Operation(summary = "test", description = "test")
    @GetMapping(value = "/test")
    @LogAnnotation
    public R<Boolean> test() {
        log.info("test");
        return new R<>();
    }
}

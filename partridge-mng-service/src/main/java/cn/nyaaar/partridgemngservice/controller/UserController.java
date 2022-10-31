package cn.nyaaar.partridgemngservice.controller;

import cn.nyaaar.partridgemngservice.common.annotation.LogAnnotation;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.model.user.RegistrationReq;
import cn.nyaaar.partridgemngservice.service.user.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "user", description = "user 相关 Controller")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    // TODO getUserCollection
    // TODO crud element to collection
    // TODO get user like elements
    // TODO get user like collection
    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Operation(summary = "注册", description = "注册")
    @PostMapping(value = "/register")
    @LogAnnotation
    public R<String> register(@RequestBody @Validated RegistrationReq request) {

        return new R<>(appUserService.register(request));
    }

    @Operation(summary = "验证 token", description = "验证 token")
    @GetMapping(value = "/confirm")
    @LogAnnotation
    public R<Boolean> confirm(@RequestParam("token") String token) {
        return new R<>(appUserService.confirmToken(token));
    }


    @Operation(summary = "ping", description = "ping")
    @GetMapping(value = "/ping")
    @LogAnnotation
    public R<String> test(@RequestParam String testString) {
        log.info("ping: " + testString);
        return new R<>("pong" + testString);
    }
}

package cn.nyaaar.partridgemngservice.service.user.impl;

import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.entity.PrUser;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.response.BaseResponse;
import cn.nyaaar.partridgemngservice.service.PrUserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆成功处理
 *
 * @author nyaaar
 * @Version $Id: CustomizeAuthenticationSuccessHandler.java, v 0.1 2022-19 17:48 nyaaar Exp $$
 */
@Component
@Slf4j
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final PrUserService prUserService;

    public CustomizeAuthenticationSuccessHandler(PrUserService prUserService) {
        this.prUserService = prUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        BaseResponse baseResponse;
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrUser prUser = prUserService.getOne(Wrappers.lambdaQuery(PrUser.class)
                .eq(PrUser::getUserName, userDetails.getUsername()));
        if (prUser == null) {
            baseResponse = new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "用户名不存在");
        } else {
            log.info("[{}] login, time:{}", prUser.getUserName(), DateUtil.date());
            baseResponse = new BaseResponse();
            prUser.setLastLoginTime(DateUtil.date());
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(baseResponse));
    }
}
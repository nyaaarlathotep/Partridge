package cn.nyaaar.partridgemngservice.service.user.impl;

import cn.hutool.core.date.DateUtil;
import cn.nyaaar.partridgemngservice.entity.PrUser;
import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.service.PrUserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆成功处理 service
 *
 * @author nyaaar
 * @Version $Id: CustomizeAuthenticationSuccessHandler.java, v 0.1 2022-19 17:48 nyaaar Exp $$
 */
@Component
public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final PrUserService prUserService;

    public CustomizeAuthenticationSuccessHandler(PrUserService prUserService) {
        this.prUserService = prUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrUser prUser = prUserService.getOne(Wrappers.lambdaQuery(PrUser.class)
                .eq(PrUser::getUserName, userDetails.getUsername()));
        BusinessExceptionEnum.USER_NOT_EXIST.assertNotNull(prUser);
        prUser.setLastLoginTime(DateUtil.date());
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new R<>()));

    }
}
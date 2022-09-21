package cn.nyaaar.partridgemngservice.service.user.impl;

import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.response.BaseResponse;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆失败处理
 *
 * @author nyaaar
 * @Version $Id: CustomizeAuthenticationFailureHandler.java, v 0.1 2022-19 17:58 nyaaar Exp $$
 */
@Component
@Slf4j
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {

        BaseResponse baseResponse;
        if (e instanceof AccountExpiredException) {
            log.error("onAuthenticationFailure: ", e);
            //账号过期
            baseResponse = new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "账号已过期");
        } else if (e instanceof BadCredentialsException) {
            log.error("onAuthenticationFailure: ", e);
            //密码错误
            baseResponse = new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "密码错误");
        } else if (e instanceof CredentialsExpiredException) {
            log.error("onAuthenticationFailure: ", e);
            baseResponse = new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "密码过期");
            //密码过期
        } else if (e instanceof DisabledException) {
            log.error("onAuthenticationFailure: ", e);
            baseResponse = new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "账号不可用");
            //账号不可用
        } else if (e instanceof LockedException) {
            log.error("onAuthenticationFailure: ", e);
            baseResponse = new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "账号被锁定");
            //账号锁定
        } else if (e instanceof InternalAuthenticationServiceException) {
            log.error("onAuthenticationFailure: ", e);
            baseResponse = new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "账号不存在");
            //用户不存在
        } else {
            log.error("onAuthenticationFailure: ", e);
            baseResponse = new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "账号其他错误");
            //其他错误
        }
        //处理编码方式，防止中文乱码的情况
        httpServletResponse.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        httpServletResponse.getWriter().write(JSON.toJSONString(baseResponse));
    }

}
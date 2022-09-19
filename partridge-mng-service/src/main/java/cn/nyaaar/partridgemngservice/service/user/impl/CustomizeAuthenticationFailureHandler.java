package cn.nyaaar.partridgemngservice.service.user.impl;

import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
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
public class CustomizeAuthenticationFailureHandler implements AuthenticationFailureHandler  {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

//        R<String> result = null;
//        if (e instanceof AccountExpiredException) {
//            //账号过期
//            result = ResultTool.fail(ResultCode.USER_ACCOUNT_EXPIRED);
//        } else if (e instanceof BadCredentialsException) {
//            //密码错误
//            result = ResultTool.fail(ResultCode.USER_CREDENTIALS_ERROR);
//        } else if (e instanceof CredentialsExpiredException) {
//            //密码过期
//            result = ResultTool.fail(ResultCode.USER_CREDENTIALS_EXPIRED);
//        } else if (e instanceof DisabledException) {
//            //账号不可用
//            result = ResultTool.fail(ResultCode.USER_ACCOUNT_DISABLE);
//        } else if (e instanceof LockedException) {
//            //账号锁定
//            result = ResultTool.fail(ResultCode.USER_ACCOUNT_LOCKED);
//        } else if (e instanceof InternalAuthenticationServiceException) {
//            //用户不存在
//            result = ResultTool.fail(ResultCode.USER_ACCOUNT_NOT_EXIST);
//        }else{
//            //其他错误
//            result = ResultTool.fail(ResultCode.COMMON_FAIL);
//        }
        if (e instanceof AccountExpiredException) {
            //账号过期
            BusinessExceptionEnum.USER_CUSTOM.assertFail("账号已过期");
        } else if (e instanceof BadCredentialsException) {
            //密码错误
            BusinessExceptionEnum.USER_CUSTOM.assertFail("密码错误");
        } else if (e instanceof CredentialsExpiredException) {
            BusinessExceptionEnum.USER_CUSTOM.assertFail("密码过期");
            //密码过期
        } else if (e instanceof DisabledException) {
            BusinessExceptionEnum.USER_CUSTOM.assertFail("账号不可用");
            //账号不可用
        } else if (e instanceof LockedException) {
            BusinessExceptionEnum.USER_CUSTOM.assertFail("账号被锁定");
            //账号锁定
        } else if (e instanceof InternalAuthenticationServiceException) {
            BusinessExceptionEnum.USER_CUSTOM.assertFail("账号不存在");
            //用户不存在
        }else{
            BusinessExceptionEnum.USER_CUSTOM.assertFail("账号其他错误");
            //其他错误
        }
//        //处理编码方式，防止中文乱码的情况
//        httpServletResponse.setContentType("text/json;charset=utf-8");
//        //塞到HttpServletResponse中返回给前台
//        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }

}
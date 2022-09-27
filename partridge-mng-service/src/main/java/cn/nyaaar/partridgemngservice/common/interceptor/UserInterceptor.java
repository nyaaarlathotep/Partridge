package cn.nyaaar.partridgemngservice.common.interceptor;

import cn.nyaaar.partridgemngservice.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * @author yuegenhua
 * @Version $Id: UserInterceptor.java, v 0.1 2022-27 9:54 yuegenhua Exp $$
 */
@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {

    /**
     * 请求执行前执行的，将用户信息放入ThreadLocal
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @return true
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        Principal principal;
        try {
            principal = request.getUserPrincipal();
        } catch (Exception e) {
            return true;
        }
        if (null != principal) {
            ThreadLocalUtil.addCurrentUser(principal.getName());
            return true;
        }
        return true;
    }

    /**
     * 接口访问结束后，从ThreadLocal中删除用户信息
     *
     * @param request  request
     * @param response response
     * @param handler  handler
     * @param ex       exception
     */
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        ThreadLocalUtil.remove();
    }

}
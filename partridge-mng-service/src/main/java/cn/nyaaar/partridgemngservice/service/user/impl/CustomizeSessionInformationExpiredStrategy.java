package cn.nyaaar.partridgemngservice.service.user.impl;

import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import cn.nyaaar.partridgemngservice.model.response.BaseResponse;
import com.alibaba.fastjson.JSON;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 会话信息过期策略
 *
 * @author nyaaar
 * @Version $Id: CustomizeSessionInformationExpiredStrategy.java, v 0.1 2022-19 16:59 nyaaar Exp $$
 */
@Component
public class CustomizeSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException {
        BaseResponse baseResponse =
                new BaseResponse(BusinessExceptionEnum.USER_CUSTOM.getCode(), "用户登录过期");
        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(baseResponse));
    }
}
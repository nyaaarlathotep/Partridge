package cn.nyaaar.partridgemngservice.common.config;

import cn.nyaaar.partridgemngservice.exception.BusinessExceptionEnum;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
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
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
//        JsonResult result = ResultTool.fail(ResultCode.USER_ACCOUNT_USE_BY_OTHERS);
//        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
//        httpServletResponse.setContentType("text/json;charset=utf-8");
//        httpServletResponse.getWriter().write(JSON.toJSONString(result));

        BusinessExceptionEnum.USER_EXPIRE.assertFail();

    }
}
package cn.nyaaar.partridgemngservice.common.advice;

import cn.nyaaar.partridgemngservice.common.annotation.LoggingApi;
import com.alibaba.fastjson.JSON;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * @author yuegenhua
 * @Version $Id: RequestBodyLogger.java, v 0.1 2023-12 17:35 yuegenhua Exp $$
 */
@ControllerAdvice
@ConditionalOnProperty(
    prefix = "partridge.webmvc",
    value = {"enabled"},
    matchIfMissing = true
)
public class RequestBodyLogger extends RequestBodyAdviceAdapter {
  private static final Logger log = LoggerFactory.getLogger(RequestBodyLogger.class);
  @Value("${saicfc.webmvc.access-logging-enabled:true}")
  private boolean accessLoggingEnabled;

  public RequestBodyLogger() {
  }

  public boolean supports(MethodParameter methodParameter, @NotNull Type type, @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
    return methodParameter.hasParameterAnnotation(RequestBody.class);
  }

  @NotNull
  public Object afterBodyRead(@NotNull Object body, @NotNull HttpInputMessage inputMessage, @NotNull MethodParameter parameter,
                              @NotNull Type targetType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
    if (this.accessLoggingEnabled || this.hasLoggingAnnotation(parameter)) {
      if (body instanceof String) {
        log.info("[HTTP-RECV]{}", body);
      } else {
        log.info("[HTTP-RECV]{}", JSON.toJSONString(body));
      }
    }
    return body;
  }

  private boolean hasLoggingAnnotation(MethodParameter parameter) {
    return parameter.hasMethodAnnotation(LoggingApi.class) || parameter.hasParameterAnnotation(LoggingApi.class) || this.hasTypeLoggingAnnotation(parameter);
  }

  private boolean hasTypeLoggingAnnotation(MethodParameter parameter) {
    return parameter.getMethod() != null && parameter.getMethod().getDeclaringClass().getAnnotation(LoggingApi.class) != null;
  }
}

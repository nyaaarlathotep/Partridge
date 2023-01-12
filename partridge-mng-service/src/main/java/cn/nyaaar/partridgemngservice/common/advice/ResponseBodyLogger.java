package cn.nyaaar.partridgemngservice.common.advice;

import cn.nyaaar.partridgemngservice.common.annotation.LoggingApi;
import cn.nyaaar.partridgemngservice.common.annotation.LoggingResponse;
import cn.nyaaar.partridgemngservice.model.response.R;
import com.alibaba.fastjson.JSON;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.MethodParameter;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuegenhua
 * @Version $Id: ResponseBodyLogger.java, v 0.1 2023-12 17:55 yuegenhua Exp $$
 */
@ControllerAdvice
@ConditionalOnProperty(
    prefix = "partridge.webmvc",
    value = {"enabled"},
    matchIfMissing = true
)
public class ResponseBodyLogger implements ResponseBodyAdvice<Object> {

  private static final Logger log = LoggerFactory.getLogger(ResponseBodyLogger.class);
  private static final ExpressionParser PARSE = new SpelExpressionParser();
  private static final ParserContext TEMPLATE_EXPRESSION = new ParserContext() {
    public boolean isTemplate() {
      return true;
    }

    @NotNull
    public String getExpressionPrefix() {
      return "#`";
    }

    @NotNull
    public String getExpressionSuffix() {
      return "`";
    }
  };
  private static final String RESPONSE_DATA_KEY = "data";
  private static final String RESPONSE_TRACE_ID_KEY = "traceId";
  private static final String RESPONSE_DATA_TPL;
  @Value("${partridge.webmvc.default-response-logging-ignore-uri-key-words:actuator}")
  private List<String> defaultResponseLoggingIgnoreUriKeyWords;
  @Value("${partridge.webmvc.response-logging-ignore-uri-key-words:}")
  private List<String> responseLoggingIgnoreUriKeyWords;
  @Value("${partridge.webmvc.rest-response-error-wrapper-enabled:false}")
  private boolean restResponseErrorWrapperEnabled;
  @Value("${partridge.webmvc.access-logging-enabled:true}")
  private boolean accessLoggingEnabled;
  @Value("${partridge.webmvc.rest-response-error-wrapper-tpl:TraceId:\t#`[traceId]`<br/>错误码:\t#`[subCode]`<br/>错误信息:\t#`[subMsg]`}")
  private String errTpl;

  public ResponseBodyLogger() {
  }

  public boolean supports(@NotNull MethodParameter returnType, @NotNull Class converterType) {
    return true;
  }

  public Object beforeBodyWrite(Object body, @NotNull MethodParameter returnType, @NotNull MediaType selectedContentType,
                                @NotNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                @NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response) {
    if (this.canLogAccess(request, returnType) && (!(response instanceof ServletServerHttpResponse)
        || ((ServletServerHttpResponse) response).getServletResponse().getStatus() != 404)) {
      log.info("[HTTP-RESP]{}", JSON.toJSONString(body));
    }

//    return this.restResponseErrorWrapperEnabled
//        && body instanceof R
//        && CommonResponseEnum.SUCCESS.getCode() != (((R<?>) body).getCode()) ? body : body;
    return body;
  }

  private boolean canLogAccess(ServerHttpRequest request, MethodParameter returnType) {
    return (this.accessLoggingEnabled || this.hasLoggingAnnotation(returnType))
        && this.isNotIgnored(this.defaultResponseLoggingIgnoreUriKeyWords, request)
        && this.isNotIgnored(this.responseLoggingIgnoreUriKeyWords, request);
  }

  private boolean hasLoggingAnnotation(MethodParameter returnType) {
    return returnType.hasMethodAnnotation(LoggingApi.class) ||
        returnType.hasMethodAnnotation(LoggingResponse.class) ||
        this.hasTypeLoggingAnnotation(returnType);
  }

  private boolean hasTypeLoggingAnnotation(MethodParameter returnType) {
    return returnType.getMethod() != null &&
        (returnType.getMethod().getDeclaringClass().getAnnotation(LoggingResponse.class) != null ||
            returnType.getMethod().getDeclaringClass().getAnnotation(LoggingApi.class) != null);
  }

  private boolean isNotIgnored(List<String> excludedUris, ServerHttpRequest request) {
    return excludedUris == null || excludedUris.isEmpty() || excludedUris.stream().noneMatch((u) -> request.getURI().toString().contains(u));
  }


  private String getErr(R<?> body) {
    BeanMap beanMap = BeanMap.create(body);
    Map<String, Object> map = new HashMap(beanMap);
    if (this.errTpl.contains(RESPONSE_DATA_TPL)) {
      map.put("data", JSON.toJSONString(body.getData()));
    }

//    map.put("traceId", ContextUtils.getCurrentTraceId());
    return this.format(this.errTpl, map);
  }

  private String format(String tpl, Map<?, ?> body) {
    return PARSE.parseExpression(tpl, TEMPLATE_EXPRESSION).getValue(body, String.class);
  }

  static {
    RESPONSE_DATA_TPL = TEMPLATE_EXPRESSION.getExpressionPrefix() + "[" + "data" + "]" + TEMPLATE_EXPRESSION.getExpressionSuffix();
  }

}
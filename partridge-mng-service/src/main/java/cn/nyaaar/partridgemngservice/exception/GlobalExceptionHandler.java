package cn.nyaaar.partridgemngservice.exception;

import cn.nyaaar.partridgemngservice.enums.CommonResponseEnum;
import cn.nyaaar.partridgemngservice.model.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        log.error("发生参数校验异常", e);

        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = "";
        if (bindingResult != null && bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError error : errorList) {
                // 取其中一个错误Message返回给前端
                errorMsg = error.getDefaultMessage();
                break;
            }
        }
        if (StringUtils.isBlank(errorMsg)) {
            errorMsg = e.getMessage();
        }
        return new BaseResponse(CommonResponseEnum.SERVER_ERROR.getCode(), errorMsg);
    }

    @ExceptionHandler(ValidationException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, ValidationException e) {
        log.error("发生校验异常", e);
        return new BaseResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    @ExceptionHandler(ArgumentException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, ArgumentException e) {
        log.error("发生参数异常", e);
        return new BaseResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, BusinessException e) {
        log.error("发生业务异常", e);
        return new BaseResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, BaseException e) {
        log.error("发生基础业务异常", e);
        return new BaseResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("发生未知异常 ", e);
        return new BaseResponse(CommonResponseEnum.SERVER_ERROR.getCode(), "服务器异常");
    }

    @ExceptionHandler(Throwable.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, Throwable e) {
        log.error("发生未知异常 ", e);
        return new BaseResponse(CommonResponseEnum.SERVER_ERROR.getCode(), "服务器异常");
    }

}

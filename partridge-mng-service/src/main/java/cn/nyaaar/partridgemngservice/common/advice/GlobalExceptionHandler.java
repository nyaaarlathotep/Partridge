package cn.nyaaar.partridgemngservice.common.advice;

import cn.nyaaar.partridgemngservice.common.enums.error.CodeEnum;
import cn.nyaaar.partridgemngservice.common.enums.error.CommonResponseEnum;
import cn.nyaaar.partridgemngservice.common.enums.error.CommonSubCodeEnum;
import cn.nyaaar.partridgemngservice.exception.ArgumentException;
import cn.nyaaar.partridgemngservice.exception.BaseException;
import cn.nyaaar.partridgemngservice.exception.BusinessException;
import cn.nyaaar.partridgemngservice.exception.ValidationException;
import cn.nyaaar.partridgemngservice.model.response.BaseResponse;
import cn.nyaaar.partridgemngservice.model.response.R;
import cn.nyaaar.partridgemngservice.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements Ordered {
    @Value("${partridge.webmvc.global-exception-logging-error:false}")
    private boolean loggingError;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        logError(e);

        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = "";
        if (bindingResult.hasErrors()) {
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError error : errorList) {
                // 取其中一个错误Message返回给前端
                if (error instanceof FieldError) {
                    errorMsg = ((FieldError) error).getField() + error.getDefaultMessage();
                } else {
                    errorMsg = error.getDefaultMessage();
                }
                break;
            }
        }
        if (!StringUtils.hasText(errorMsg)) {
            errorMsg = e.getMessage();
        }
        return new BaseResponse(CodeEnum.PRECONDITION_FAILED.getCode(), errorMsg);
    }

    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class})
    public BaseResponse ValidationExceptionHandler(HttpServletRequest req, Exception e) {
        logError(e);
        return new BaseResponse(CodeEnum.ERROR.getCode(), CodeEnum.ERROR.getMsg());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Object adviceConstraintViolationException(ConstraintViolationException e) {
        logError(e);
        String msg = null;
        if (e.getConstraintViolations().size() > 0) {
            ConstraintViolation<?> next = e.getConstraintViolations().iterator().next();
            msg = String.format("%s:%s", next.getPropertyPath().toString(), next.getMessage());
        }
        R<?> resp = new R<>();
        resp.setCode(CodeEnum.PRECONDITION_FAILED.getCode());
        resp.setSubCode(CommonSubCodeEnum.PARAM_NOT_VALID.getSubCode());
        resp.setSubMsg(msg);
        return resp;
    }

    @ExceptionHandler(ArgumentException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, ArgumentException e) {
        logError(e);
        return new BaseResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, BusinessException e) {
        log.error("", e);
        return new BaseResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, BaseException e) {
        log.error("", e);
        return new BaseResponse(e.getResponseEnum().getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("", e);
        R<?> resp = new R<>();

        if (ExceptionUtils.isCode(e.getMessage())) {
            String[] codes = ExceptionUtils.extractCode(e.getMessage());
            resp.setCode(Integer.parseInt(codes[0]));
            resp.setSubCode(codes[1]);
        } else {
            Throwable lastCause = e.getCause() == null ? e : e.getCause();
            Throwable t;
            while ((t = traceCause(lastCause)) != null && t != lastCause) {
                lastCause = t;
            }
            if (lastCause.getMessage() != null && lastCause.getMessage().contains("ORA-")) {
                resp.setCode(CodeEnum.PRECONDITION_FAILED.getCode());
                dealWithOraSubError(resp, lastCause);
            } else {
                resp.setCode(CodeEnum.ERROR.getCode());
                resp.setSubCode(CommonSubCodeEnum.ERROR_OCCURRED.getSubCode());
            }
        }
        resp.setSubMsg(resp.getSubMsg());
        return resp;
    }

    @ExceptionHandler(Throwable.class)
    public BaseResponse exceptionHandler(HttpServletRequest req, Throwable e) {
        log.error("发生未知异常 ", e);
        return new BaseResponse(CommonResponseEnum.SERVER_ERROR.getCode(), "服务器异常，请联系管理员");
    }

    private Throwable traceCause(Throwable e) {
        return e == null ? null : e.getCause();
    }

    private void dealWithOraSubError(R<?> resp, Throwable t) {
        if (t.getMessage().contains("ORA-00001")) {
            resp.setSubCode(CommonSubCodeEnum.DB_ERROR_00001.getSubCode());
        } else if (t.getMessage().contains("ORA-01438")) {
            resp.setSubCode(CommonSubCodeEnum.DB_ERROR_01438.getSubCode());
        } else if (t.getMessage().contains("ORA-00904")) {
            resp.setSubCode(CommonSubCodeEnum.DB_ERROR_00904.getSubCode());
        } else {
            resp.setSubCode(CommonSubCodeEnum.DB_ERROR_00000.getSubCode());
        }
    }

    private void logError(Exception e) {
        if (loggingError) {
            log.error("", e);
        }
    }
}

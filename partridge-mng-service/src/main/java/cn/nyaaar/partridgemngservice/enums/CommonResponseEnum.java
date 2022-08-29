package cn.nyaaar.partridgemngservice.enums;

import cn.nyaaar.partridgemngservice.exception.BaseException;
import cn.nyaaar.partridgemngservice.exception.CommonExceptionAssert;
import cn.nyaaar.partridgemngservice.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>通用返回结果</p>
 */
@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements CommonExceptionAssert {
    /**
     * 成功
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 服务器繁忙，请稍后重试
     */
    SERVER_BUSY(9998, "服务器繁忙"),
    /**
     * 服务器异常，无法识别的异常，尽可能对通过判断减少未定义异常抛出
     */
    SERVER_ERROR(9999, "服务异常,请复制以上错误码后联系运维人员"),

    /**
     * 5*** 系统封装的工具出现异常
     */

    // Time
    DATE_NOT_NULL(5001, "日期不能为空"),
    DATETIME_NOT_NULL(5001, "时间不能为空"),
    TIME_NOT_NULL(5001, "时间不能为空"),
    DATE_PATTERN_MISMATCH(5002, "日期[%s]与格式[%s]不匹配，无法解析"),
    PATTERN_NOT_NULL(5003, "日期格式不能为空"),
    PATTERN_INVALID(5003, "日期格式[%s]无法识别"),
    ;

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;

    /**
     * 校验返回结果是否成功
     *
     * @param response 远程调用的响应
     */
    public static void assertSuccess(BaseResponse response) throws BaseException {
        SERVER_ERROR.assertNotNull(response);
        int code = response.getCode();
        if (CommonResponseEnum.SUCCESS.getCode() != code) {
            String msg = response.getMessage();
            throw new BaseException(code, msg);
        }
    }
}

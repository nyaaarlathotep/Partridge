package cn.nyaaar.partridgemngservice.exception;

import cn.nyaaar.partridgemngservice.exception.assertion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 业务异常枚举
 * @author: yangyang3
 * @date: 2021/7/23
 */
@AllArgsConstructor
@Getter
public enum BusinessExceptionEnum implements BusinessExceptionAssert {

    // 通用业务异常
    FIELD_NULL(100001, "{0}不能为空"),
    NOT_EXISTS(100002, "{0}不存在"),
    FIELD_ERROR(100003, "{0}错误"),
    FIELD_ER_WITH_ER_VALUE(100004, "{0}错误, 错误值{1}"),
    FIELD_ER_WITH_CO_VALUE(100005, "{0}错误, 正确值{1}"),
    INVOKE_ERROR(100006, "错误描述:{0}"),
    JSON_PARSE_ERR(100007, "JSON转换错误"),
    DECRYPTION_ERR(100008, "请求数据错误，无法解密"),
    BAD_REQUEST(100009, "请求异常"),
    VERIFY_SIGN_ERR(100010, "验签失败"),
    FIELD_ERROR_CUSTOM(100011, "{0}"),
    SYSTEM_ERROR_CUSTOM(100012, "{0}"),
    HTTP_REQUEST_FAILED(100013, "网络异常"),
    SYSTEM_DATA_ERROR(100014, "{0}"),
    COMMON_BUSINESS_ERROR(100015, "{0}"),
    TIME_FORMAT_ERROR(100016, "时间格式异常{0}"),
    ENCRYPTION_ERROR(1000017, "发送数据错误，无法加密"),

    // 特殊业务异常
    NOT_FOUND_IDCARD(200001, "未检测到身份证，请重新上传！"),
    IDPIC_TYPE_ERROR(200002, "{0}"),
    IDPIC_OCR_ERROR(200003, "{0}"),
    ID_NO_ERROR(200004, "{0}"),
    PIC_UPLOAD_FAIL(200005, "照片上传失败"),
    IDPIC_FETCH_FAIL(200006, "身份证照片获取失败"),
    ID_VERIFY_FAIL(200007, "{0}"),
    PHONE_NO_ERROR(200008, "{0}"),
    STATE_ERROR(200009, "贷款状态错误，当前为 {0} 状态");


    int code;

    String message;

}

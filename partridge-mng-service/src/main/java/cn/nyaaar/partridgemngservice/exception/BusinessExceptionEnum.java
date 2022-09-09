package cn.nyaaar.partridgemngservice.exception;

import cn.nyaaar.partridgemngservice.exception.assertion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BusinessExceptionEnum implements BusinessExceptionAssert {

    // 通用业务异常
    FIELD_NULL(100001, "{0}不能为空"),
    NOT_EXISTS(100002, "{0}不存在"),
    FIELD_ERROR(100003, "{0}错误"),
    FIELD_ER_WITH_ER_VALUE(100004, "{0}错误, 错误值: {1}"),
    FIELD_ER_WITH_CO_VALUE(100005, "{0}错误, 正确值: {1}"),
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
    IDPIC_TYPE_ERROR(200002, "{0}"),
    IDPIC_OCR_ERROR(200003, "{0}"),
    ID_NO_ERROR(200004, "{0}"),

    ID_VERIFY_FAIL(200007, "{0}"),
    PHONE_NO_ERROR(200008, "{0}"),
    PARSE_ERROR(200010, "parse error, {0}"),
    SAD_PANDA(200011, "Sad Panda"),
    SAD_PANDA_WITHOUT(200011, "Sad Panda(without panda)"),
    END_HERE(200012, "今回はここまで"),
    GALLERY_NOT_AVAILABLE(200013, "{0}"),
    ELEMENT_NOT_FOUND(200014, "没有找到对应元素"),
    GALLERY_NOT_FOUND(200015, "没有找到对应画廊"),
    PAGE_NOT_FOUND(200016, "没有找到对应页码"),
    ;

    final int code;

    final String message;

}

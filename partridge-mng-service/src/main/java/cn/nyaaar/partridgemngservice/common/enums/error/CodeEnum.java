package cn.nyaaar.partridgemngservice.common.enums.error;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuegenhua
 * @Version $Id: CodeEnum.java, v 0.1 2023-16 10:23 yuegenhua Exp $$
 */
public enum CodeEnum implements IResponseEnum {
    SUCCESS(200, "Success"),
    ACCEPT(202, "Request accepted, we'll do it later."),
    BAD_REQUEST(400, "Bad request"),
    VERIFY_FAILED(401, "Signature verify failure"),
    FORBID_BIZ(403, "No authority to use this method."),
    TARGET_NOT_FOUND(404, "Target not found."),
    FORBID_METHOD(405, "Action not supported."),
    TIMEOUT(408, "Timeout"),
    CONFLICT(409, "Conflict failure, maybe data exists."),
    PRECONDITION_FAILED(412, "Parameter verify failed."),
    ERROR(500, "System error."),
    GATEWAY_TIMEOUT(502, "Gateway timeout."),
    SYS_BUSY(503, "System busy or service unavailable");

    private final int code;
    private final String msg;
    private static final Map<Integer, CodeEnum> MAP = new HashMap<>(20);

    public static CodeEnum fromCode(Integer code) {
        return MAP.get(code);
    }

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return getMsg();
    }

    public String getMsg() {
        return this.msg;
    }

    static {
        CodeEnum[] var0 = values();

        for (CodeEnum e : var0) {
            MAP.put(e.getCode(), e);
        }

    }
}
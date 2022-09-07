package cn.nyaaar.partridgemngservice.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 来源枚举类
 *
 * @author nyaaar
 * @Version $Id: SourceCodeEnum.java, v 0.1 2022-01 9:42 nyaaar Exp $$
 */
public enum SourceEnum {
    Jav("1", "Jav"),
    Ehentai("2", "ehentai"),
    ;

    private final String code;
    private final String msg;
    private static final Map<String, SourceEnum> MAP = new HashMap<>(10);

    SourceEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static SourceEnum fromCode(String code) {
        return MAP.get(code);
    }

    /**
     * Getter method for property <tt>code<tt>.
     *
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Getter method for property <tt>msg<tt>.
     *
     * @return property value of msg
     */
    public String getMsg() {
        return msg;
    }

    static {
        SourceEnum[] sourceEnums = values();

        for (SourceEnum e : sourceEnums) {
            MAP.put(e.getCode(), e);
        }
    }
}
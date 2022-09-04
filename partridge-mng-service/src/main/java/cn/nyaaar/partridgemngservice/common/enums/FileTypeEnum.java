package cn.nyaaar.partridgemngservice.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型枚举类
 *
 * @author yuegenhua
 * @Version $Id: FileTypeEnum.java, v 0.1 2022-01 17:58 yuegenhua Exp $$
 */
public enum FileTypeEnum {
    jpg("1", ".jpg"),
    mp4("2", ".mp4"),
    avi("3", ".avi"),
    ;

    private final String code;
    private final String suffix;
    private static final Map<String, FileTypeEnum> MAP = new HashMap<>(10);

    FileTypeEnum(String code, String suffix) {
        this.code = code;
        this.suffix = suffix;
    }

    public static FileTypeEnum fromCode(String code) {
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
    public String getSuffix() {
        return suffix;
    }

    static {
        FileTypeEnum[] fileTypeEnums = values();

        for (FileTypeEnum e : fileTypeEnums) {
            MAP.put(e.getCode(), e);
        }
    }
}
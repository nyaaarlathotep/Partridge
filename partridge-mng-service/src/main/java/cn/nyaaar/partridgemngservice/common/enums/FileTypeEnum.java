package cn.nyaaar.partridgemngservice.common.enums;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件类型枚举类
 *
 * @author nyaaar
 * @Version $Id: FileTypeEnum.java, v 0.1 2022-01 17:58 nyaaar Exp $$
 */
public enum FileTypeEnum {
    unknown("0", ".unknown"),
    jpg("1", ".jpg"),
    gif("2", ".gif"),
    mp4("3", ".mp4"),
    avi("4", ".avi"),
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

    public static FileTypeEnum getTypeBySuffix(@NotNull String name) {
        FileTypeEnum[] fileTypeEnums = values();
        for (FileTypeEnum e : fileTypeEnums) {
            if (name.endsWith(e.suffix)) {
                return e;
            }
        }
        return unknown;
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
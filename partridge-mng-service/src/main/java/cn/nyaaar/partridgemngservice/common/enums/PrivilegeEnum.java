package cn.nyaaar.partridgemngservice.common.enums;

import lombok.Getter;

@Getter
public enum PrivilegeEnum {

    USER("user", "user"),
    ROOT("root", "root"),
    ;

    private final String code;
    private final String desc;

    PrivilegeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

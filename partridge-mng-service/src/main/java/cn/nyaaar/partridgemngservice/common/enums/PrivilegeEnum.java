package cn.nyaaar.partridgemngservice.common.enums;

import lombok.Getter;

@Getter
public enum PrivilegeEnum {

    USER("USER", "user"),
    ROOT("ROOT", "root"),
    ;

    private final String code;
    private final String desc;

    PrivilegeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

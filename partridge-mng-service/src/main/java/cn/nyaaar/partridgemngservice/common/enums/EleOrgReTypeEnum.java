package cn.nyaaar.partridgemngservice.common.enums;

public enum EleOrgReTypeEnum {
    publish("publish", "发行"),
    produce("produce", "制作"),
    ;

    private final String re;

    private final String desc;

    EleOrgReTypeEnum(String re, String desc) {
        this.re = re;
        this.desc = desc;
    }

    public String getRe() {
        return re;
    }

    public String getDesc() {
        return desc;
    }
}

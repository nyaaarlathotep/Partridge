package cn.nyaaar.partridgemngservice.common.enums;


public enum CompleteFlagEnum {
    DUMMY(0, "DUMMY"),
    INFO_ONLY(1, "INFO_ONLY"),
    UPLOADING(2, "UPLOADING"),
    UPLOADED(3, "UPLOADED"),
    DOWNLOADING(4, "DOWNLOADING"),
    DOWNLOADED(5, "DOWNLOADED"),
    ;

    private final Integer code;

    private final String desc;

    CompleteFlagEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean completed(Integer code) {
        return code.equals(UPLOADED.getCode()) || code.equals(DOWNLOADED.getCode());
    }
}
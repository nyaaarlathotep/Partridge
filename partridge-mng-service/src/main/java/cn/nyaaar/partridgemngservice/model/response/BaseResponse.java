package cn.nyaaar.partridgemngservice.model.response;

import cn.nyaaar.partridgemngservice.common.enums.error.CommonResponseEnum;
import cn.nyaaar.partridgemngservice.common.enums.error.IResponseEnum;
import lombok.Data;

/**
 * <p>基础返回结果</p>
 */
@Data
public class BaseResponse {
    /**
     * 返回码
     */
    protected int code;
    /**
     * 次级返回码
     */
    protected String subCode;
    /**
     * 返回消息
     */
    protected String message;
    /**
     * 次级返回消息
     */
    protected String subMsg;

    public BaseResponse() {
        // 默认创建成功的回应
        this(CommonResponseEnum.SUCCESS);
    }

    public BaseResponse(IResponseEnum responseEnum) {
        this(responseEnum.getCode(), responseEnum.getMessage());
    }

    public BaseResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
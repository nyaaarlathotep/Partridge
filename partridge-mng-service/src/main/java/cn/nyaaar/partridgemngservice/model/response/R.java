package cn.nyaaar.partridgemngservice.model.response;

/**
 * 通用返回结果
 *
 * @author nyaaar
 * @Version $Id: R.java, v 0.1 2022-22 15:08 nyaaar Exp $$
 */

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class R<T> extends CommonResponse<T> {

    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(T data, String msg) {
        super();
        this.data = data;
        this.message = msg;
    }

    public R(Throwable e) {
        super();
        this.message = e.getMessage();
        this.code = -1;
    }
}

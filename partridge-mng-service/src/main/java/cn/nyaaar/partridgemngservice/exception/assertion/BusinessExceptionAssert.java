package cn.nyaaar.partridgemngservice.exception.assertion;

import cn.nyaaar.partridgemngservice.common.enums.error.IResponseEnum;
import cn.nyaaar.partridgemngservice.exception.BaseException;
import cn.nyaaar.partridgemngservice.exception.BusinessException;

import java.text.MessageFormat;

/**
 * <p>业务异常断言</p>
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        throw new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        throw new BusinessException(this, args, msg, t);
    }

}

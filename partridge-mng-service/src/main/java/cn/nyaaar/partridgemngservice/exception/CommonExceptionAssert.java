package cn.nyaaar.partridgemngservice.exception;

import cn.nyaaar.partridgemngservice.enums.IResponseEnum;
import cn.nyaaar.partridgemngservice.exception.assertion.Assert;

import java.text.MessageFormat;

/**
 * <pre>
 *
 * </pre>
 */
public interface CommonExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new ArgumentException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new ArgumentException(this, args, msg, t);
    }

}

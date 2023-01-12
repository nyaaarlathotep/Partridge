package cn.nyaaar.partridgemngservice.common.annotation;

import java.lang.annotation.*;

/**
 * @author yuegenhua
 * @Version $Id: LoggingApi.java, v 0.1 2023-12 17:36 yuegenhua Exp $$
 */

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggingApi {
}

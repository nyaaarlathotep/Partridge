package cn.nyaaar.partridgemngservice.common.annotation;

import java.lang.annotation.*;

/**
 * @author yuegenhua
 * @Version $Id: LoggingResponse.java, v 0.1 2023-12 18:05 yuegenhua Exp $$
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggingResponse {
}
package cn.nyaaar.partridgemngservice.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    /**
     * 模块
     */
    String title() default "";

    /**
     * 功能
     */
    String action() default "";

    /**
     * 忽略参数打印
     */
    boolean omitParam() default false;

    /**
     * 忽略响应结果打印
     */
    boolean omitRes() default false;
}

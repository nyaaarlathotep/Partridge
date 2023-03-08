package cn.nyaaar.partridgemngservice.common.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupField {

    String[] value() default {};
}

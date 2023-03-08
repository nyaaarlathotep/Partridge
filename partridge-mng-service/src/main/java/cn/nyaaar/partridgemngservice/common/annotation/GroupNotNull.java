package cn.nyaaar.partridgemngservice.common.annotation;

import cn.nyaaar.partridgemngservice.common.validation.GroupNotNullConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {GroupNotNullConstraintValidator.class})
public @interface GroupNotNull {

    GroupField[] fields() default {};

    String message() default "{GroupNotNull}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
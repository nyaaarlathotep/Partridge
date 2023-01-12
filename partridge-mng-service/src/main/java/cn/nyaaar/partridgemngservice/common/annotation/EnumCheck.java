package cn.nyaaar.partridgemngservice.common.annotation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.reflect.Field;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Constraint(validatedBy = EnumCheck.EnumCheckHandle.class)
public @interface EnumCheck {

  String NAME = "NAME";

  String CODE = "code";

  Class<? extends Enum> value();

  /**
   * 该参数表示只能接收枚举类意外的值
   */
  boolean inEnums() default true;

  /**
   * 枚举代码所指属性
   */
  String checkType() default CODE;

  /**
   * 非空属性
   */
  boolean notNull() default false;

  String message() default "字段内容非法";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class EnumCheckHandle implements ConstraintValidator<EnumCheck, String>, Annotation {

    EnumCheck enumCheck;

    @Override
    public void initialize(EnumCheck enumCheck) {
      this.enumCheck = enumCheck;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
      return EnumCheck.class;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

      if (value == null || "".equals(value)) {
        return !enumCheck.notNull();
      }

      boolean in = enumCheck.inEnums();

      Class<? extends Enum> enumClass = enumCheck.value();

      Enum<?>[] values = enumClass.getEnumConstants();

      //根据枚举名
      if (NAME.equals(enumCheck.checkType())) {

        for (Enum<?> e : values) {
          if (e.name().equals(value)) {
            return in;
          }
        }

      } else {
        for (Enum<?> e : values) {
          try {
            Field f = e.getClass().getDeclaredField(enumCheck.checkType());
            f.setAccessible(true);
            String v = (String) f.get(e);
            if (v.equals(value)) {
              return in;
            }
          } catch (Exception ignored) {

          }
        }
      }
      return false;
    }
  }
}

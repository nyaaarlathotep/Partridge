package cn.nyaaar.partridgemngservice.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidateUtils {

    private ValidateUtils() {}

    /**
     * 验证器
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验实体，返回实体所有属性的校验结果
     */
    public static <T> ValidationResult validateEntity(T obj) {
        //解析校验结果
        Set<ConstraintViolation<T>> validateSet = validator.validate(obj, Default.class);
        return buildValidationResult(validateSet);
    }

    /**
     * 校验实体，返回实体所有属性的校验结果
     */
    public static <T> ValidationResult validateEntity(T obj, Class<?>... groups) {
        //解析校验结果
        Set<ConstraintViolation<T>> validateSet = validator.validate(obj, groups);
        return buildValidationResult(validateSet);
    }

    /**
     * 校验指定实体的指定属性是否存在异常
     */
    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        Set<ConstraintViolation<T>> validateSet = validator.validateProperty(obj, propertyName, Default.class);
        return buildValidationResult(validateSet);
    }

    /**
     * 校验列表，返回列表中所有实体。一般其中某个实体校验失败，立即返回校验失败结果。
     */
    public static <T> ValidationResult validateList(List<T> objList) {
        if(CollectionUtils.isEmpty(objList)) {
            return new ValidationResult();
        }

        for(T obj: objList) {
            ValidationResult validationResult = validateEntity(obj);
            if(validationResult.isHasErrors()) {
                return validationResult;
            }
        }

        return new ValidationResult();
    }

    public static <T> ValidationResult validateList(List<T> objList, Class<?>... groups) {
        if(CollectionUtils.isEmpty(objList)) {
            return new ValidationResult();
        }

        for(T obj: objList) {
            ValidationResult validationResult = validateEntity(obj, groups);
            if(validationResult.isHasErrors()) {
                return validationResult;
            }
        }

        return new ValidationResult();
    }

    /**
     * 将异常结果封装返回
     */
    private static <T> ValidationResult buildValidationResult(Set<ConstraintViolation<T>> validateSet) {
        ValidationResult validationResult = new ValidationResult();
        if (!CollectionUtils.isEmpty(validateSet)) {
            validationResult.setHasErrors(true);
            Map<String, String> errorMsgMap = new HashMap<>();
            for (ConstraintViolation<T> constraintViolation : validateSet) {
                errorMsgMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
            validationResult.setErrorMsg(errorMsgMap);
        }
        return validationResult;
    }


    /**
     * 是否是数值型
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if(StringUtils.isEmpty(str)){
            return false;
        }

        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 是否为非负整数
     * @param str
     * @return
     */
    public static boolean isPositiveNumber(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("^[0-9]*$");
        String intStr;
        try {
            intStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(intStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}

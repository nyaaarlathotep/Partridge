package cn.nyaaar.partridgemngservice.common.validation;

import cn.nyaaar.partridgemngservice.common.annotation.GroupField;
import cn.nyaaar.partridgemngservice.common.annotation.GroupNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class GroupNotNullConstraintValidator implements ConstraintValidator<GroupNotNull, Object> {

    private static final Logger log =
            LogManager.getLogManager().getLogger(GroupNotNullConstraintValidator.class.getName());

    private GroupField[] fields;

    @Override
    public void initialize(GroupNotNull constraintAnnotation) {

        fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null || checkValue(value);
    }

    private boolean checkValue(Object value) {
        for (GroupField field : fields) {
            String[] fieldNames = field.value();
            if (fieldNames != null && fieldNames.length > 0) {
                int l = fieldNames.length;
                for (String fieldName : fieldNames) {
                    try {
                        if (value.getClass().getMethod(getMethod(fieldName)).invoke(value) == null) {
                            break;
                        }
                        l--;
                    } catch (NoSuchMethodException e) {
                        log.severe(e.getMessage());
                        throw new RuntimeException("GroupNotNull no such field");
                    } catch (InvocationTargetException e) {
                        log.severe(e.getLocalizedMessage());
                        throw new RuntimeException("GroupNotNull no such field");
                    } catch (IllegalAccessException e) {
                        log.severe(e.getLocalizedMessage());
                        throw new RuntimeException("GroupNotNull no such field");
                    }
                }
                if (l == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getMethod(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}

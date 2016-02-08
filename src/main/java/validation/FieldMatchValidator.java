package validation;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(FieldMatch constraintAnnotation)
    {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context)
    {
        try
        {
            Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            Object secondObj = BeanUtils.getProperty(value, secondFieldName);

            return firstObj != null && secondObj != null && firstObj.equals(secondObj);
        }
        catch (Exception e)
        {
            // TODO: Добавить логирование
        }

        return false;
    }
}
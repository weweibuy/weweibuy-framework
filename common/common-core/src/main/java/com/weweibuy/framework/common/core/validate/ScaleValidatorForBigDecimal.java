package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.validate.annotation.Scale;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * @author durenhao
 * @date 2023/4/16 10:14
 **/
public class ScaleValidatorForBigDecimal implements ConstraintValidator<Scale, BigDecimal> {

    protected Integer scale;


    @Override
    public void initialize(Scale constraintAnnotation) {
        this.scale = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        int scale1 = value.scale();
        return false;
    }


}

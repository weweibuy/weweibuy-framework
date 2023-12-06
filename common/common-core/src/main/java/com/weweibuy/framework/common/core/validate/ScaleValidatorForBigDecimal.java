package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.validate.annotation.Scale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * @author durenhao
 * @date 2023/4/16 10:14
 **/
public class ScaleValidatorForBigDecimal implements ConstraintValidator<Scale, BigDecimal> {

    protected int scaleMax;

    protected int scaleMin;


    @Override
    public void initialize(Scale constraintAnnotation) {
        this.scaleMax = constraintAnnotation.max();
        this.scaleMin = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        int scale = value.scale();
        return scaleMin <= scale && scale <= scaleMax;
    }


}

package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.validate.annotation.Minimum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author durenhao
 * @date 2021/1/12 21:44
 **/
public abstract class AbstractMinimumValidator<T> implements ConstraintValidator<Minimum, T> {

    protected long minValue;

    @Override
    public void initialize(Minimum maxValue) {
        this.minValue = maxValue.value();
    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        return compare(value) > 0;
    }

    protected abstract int compare(T number);

}

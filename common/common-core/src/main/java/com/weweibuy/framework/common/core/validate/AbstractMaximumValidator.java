/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.validate.annotation.Maximum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author durenhao
 * @date 2021/1/12 21:44
 **/
public abstract class AbstractMaximumValidator<T> implements ConstraintValidator<Maximum, T> {

    protected long maxValue;

    @Override
    public void initialize(Maximum maxValue) {
        this.maxValue = maxValue.value();
    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext constraintValidatorContext) {
        // null values are valid
        if (value == null) {
            return true;
        }
        return compare(value) < 0;
    }

    protected abstract int compare(T number);
}

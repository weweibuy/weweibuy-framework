package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.utils.NumberComparatorUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

/**
 * @author durenhao
 * @date 2021/1/12 21:56
 **/
public class MaximumValidatorForNumber extends AbstractMaximumValidator<Number> {

    @Override
    protected int compare(Number number) {
        return NumberComparatorUtils.compare(number, maxValue, InfinityNumberComparatorHelper.GREATER_THAN);
    }
}

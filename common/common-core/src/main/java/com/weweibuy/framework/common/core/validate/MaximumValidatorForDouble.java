package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.utils.NumberComparatorUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

/**
 * @author durenhao
 * @date 2021/1/12 21:55
 **/
public class MaximumValidatorForDouble extends AbstractMaximumValidator<Double> {

    @Override
    protected int compare(Double number) {
        return NumberComparatorUtils.compare(number, maxValue, InfinityNumberComparatorHelper.GREATER_THAN);
    }
}

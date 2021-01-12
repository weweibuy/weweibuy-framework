package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.utils.NumberComparatorUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

/**
 * @author durenhao
 * @date 2021/1/12 21:55
 **/
public class MinimumValidatorForDouble extends AbstractMinimumValidator<Double> {

    @Override
    protected int compare(Double number) {
        return NumberComparatorUtils.compare(number, minValue, InfinityNumberComparatorHelper.LESS_THAN);
    }
}

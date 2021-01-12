package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.utils.NumberComparatorUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

/**
 * @author durenhao
 * @date 2021/1/12 21:56
 **/
public class MaximumValidatorForFloat extends AbstractMaximumValidator<Float> {
    @Override
    protected int compare(Float number) {
        return NumberComparatorUtils.compare(number, maxValue, InfinityNumberComparatorHelper.GREATER_THAN);
    }
}

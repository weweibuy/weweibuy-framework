package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.utils.NumberComparatorUtils;

/**
 * @author durenhao
 * @date 2021/1/12 21:47
 **/
public class MinimumValidatorForLong extends AbstractMinimumValidator<Long> {

    @Override
    protected int compare(Long number) {
        return NumberComparatorUtils.compare(number, minValue);
    }
}

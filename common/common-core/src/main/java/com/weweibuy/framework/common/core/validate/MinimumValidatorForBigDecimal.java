package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.utils.NumberComparatorUtils;

import java.math.BigDecimal;

/**
 * @author durenhao
 * @date 2021/1/12 21:47
 **/
public class MinimumValidatorForBigDecimal extends AbstractMinimumValidator<BigDecimal> {

    @Override
    protected int compare(BigDecimal number) {
        return NumberComparatorUtils.compare(number, minValue);
    }
}

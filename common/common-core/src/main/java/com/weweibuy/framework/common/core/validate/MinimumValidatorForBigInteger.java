package com.weweibuy.framework.common.core.validate;

import com.weweibuy.framework.common.core.utils.NumberComparatorUtils;

import java.math.BigInteger;

/**
 * @author durenhao
 * @date 2021/1/12 21:47
 **/
public class MinimumValidatorForBigInteger extends AbstractMinimumValidator<BigInteger> {

    @Override
    protected int compare(BigInteger number) {
        return NumberComparatorUtils.compare(number, minValue);
    }
}

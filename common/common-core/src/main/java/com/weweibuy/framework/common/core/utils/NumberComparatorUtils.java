package com.weweibuy.framework.common.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.number.InfinityNumberComparatorHelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.OptionalInt;

/**
 * 数字比较工具
 *
 * @author durenhao
 * @date 2021/1/12 21:59
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberComparatorUtils {

    public static int compare(BigDecimal number, long value) {
        return number.compareTo(BigDecimal.valueOf(value));
    }

    public static int compare(BigInteger number, long value) {
        return number.compareTo(BigInteger.valueOf(value));
    }

    public static int compare(Long number, long value) {
        return number.compareTo(value);
    }

    public static int compare(Number number, long value, OptionalInt treatNanAs) {
        // In case of comparing numbers before we compare them as two longs:
        // 1. We need to check for floating point number as it should be treated differently in such case:
        if (number instanceof Double) {
            return compare((Double) number, value, treatNanAs);
        }
        if (number instanceof Float) {
            return compare((Float) number, value, treatNanAs);
        }

        // 2. We need to check for big numbers so that we don't lose data when converting them to long:
        if (number instanceof BigDecimal) {
            return compare((BigDecimal) number, value);
        }
        if (number instanceof BigInteger) {
            return compare((BigInteger) number, value);
        }

        return Long.compare(number.longValue(), value);
    }

    public static int compare(Double number, long value, OptionalInt treatNanAs) {
        OptionalInt infinity = InfinityNumberComparatorHelper.infinityCheck(number, treatNanAs);
        if (infinity.isPresent()) {
            return infinity.getAsInt();
        }
        return Double.compare(number, value);
    }

    public static int compare(Float number, long value, OptionalInt treatNanAs) {
        OptionalInt infinity = InfinityNumberComparatorHelper.infinityCheck(number, treatNanAs);
        if (infinity.isPresent()) {
            return infinity.getAsInt();
        }
        return Float.compare(number, value);
    }

}

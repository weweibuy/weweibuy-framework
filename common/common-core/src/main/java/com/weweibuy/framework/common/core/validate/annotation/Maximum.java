package com.weweibuy.framework.common.core.validate.annotation;

import com.weweibuy.framework.common.core.validate.*;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验值 最大值必须小于  value
 * <p>
 * Supported types are:
 * <ul>
 * <li>{@code BigDecimal}</li>
 * <li>{@code BigInteger}</li>
 * <li>{@code byte}, {@code short}, {@code int}, {@code long}, and their respective
 * wrappers</li>
 * </ul>
 * Note that {@code double} and {@code float} are not supported due to rounding errors
 *
 * @author durenhao
 * @date 2021/1/12 22:03
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Maximum.List.class)
@Documented
@Constraint(validatedBy = {MaximumValidatorForBigDecimal.class, MaximumValidatorForBigInteger.class, MaximumValidatorForDouble.class,
        MaximumValidatorForFloat.class, MaximumValidatorForLong.class, MaximumValidatorForNumber.class})
public @interface Maximum {


    String message() default "{com.weweibuy.framework.common.core.validate.annotation.Maximum.message}";

    Class<?>[] groups() default {};

    /**
     * @return value the element must be lower or equal to
     */
    long value();

    Class<? extends Payload>[] payload() default {};


    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Maximum[] value();
    }


}

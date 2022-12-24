package com.weweibuy.framework.common.core.validate.annotation;

import com.weweibuy.framework.common.core.validate.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 检验 值大于
 * *Supported types are:
 * <ul>
 *      <li>{@code BigDecimal}</li>
 *      <li>{@code BigInteger}</li>
 *      <li>{@code byte}, {@code short}, {@code int}, {@code long}, and their respective
 * wrappers</li>
 * </ul>
 *
 * @author durenhao
 * @date 2021/1/12 21:38
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Minimum.List.class)
@Documented
@Constraint(validatedBy = {MinimumValidatorForBigDecimal.class, MinimumValidatorForBigInteger.class, MinimumValidatorForDouble.class,
        MinimumValidatorForFloat.class, MinimumValidatorForLong.class, MinimumValidatorForNumber.class})
public @interface Minimum {


    String message() default "{com.weweibuy.framework.common.core.validate.annotation.Minimum.message}";

    Class<?>[] groups() default {};

    /**
     * @return value the element must be higher
     */
    long value();

    Class<? extends Payload>[] payload() default { };

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Minimum[] value();
    }

}

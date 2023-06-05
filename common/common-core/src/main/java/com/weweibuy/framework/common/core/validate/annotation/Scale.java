package com.weweibuy.framework.common.core.validate.annotation;

import com.weweibuy.framework.common.core.validate.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 校验 BigDecimal 的精度
 *
 * @author durenhao
 * @date 2023/4/16 10:12
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Scale.List.class)
@Documented
@Constraint(validatedBy = {ScaleValidatorForBigDecimal.class})
public @interface Scale {

    String message() default "{com.weweibuy.framework.common.core.validate.annotation.Scale.message}";

    Class<?>[] groups() default {};

    /**
     * 精度
     *
     * @return
     */
    int value() default 0;

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Scale[] value();
    }

}

package com.weweibuy.framework.common.util.csv.annotation;

import com.weweibuy.framework.common.util.csv.CsvTypeConverter;
import com.weweibuy.framework.common.util.csv.SimpleCsvTypeConverter;

import java.lang.annotation.*;

/**
 * @author durenhao
 * @date 2021/1/5 22:24
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvHead {

    String name();

    int index() default Integer.MAX_VALUE;

    Class<? extends CsvTypeConverter> converter() default SimpleCsvTypeConverter.class;

}

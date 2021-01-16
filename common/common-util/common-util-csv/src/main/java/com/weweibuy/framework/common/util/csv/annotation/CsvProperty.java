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
public @interface CsvProperty {

    /**
     * 标题名称
     *
     * @return
     */
    String name() default "";

    /**
     * 读取索引, 从 0 开始
     *
     * @return
     */
    int index() default Integer.MAX_VALUE;

    /**
     * 写出顺序
     *
     * @return
     */
    int order() default Integer.MAX_VALUE;

    /**
     * 类型转化
     *
     * @return
     */
    Class<? extends CsvTypeConverter> converter() default SimpleCsvTypeConverter.class;

}

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
     * csv头, 标题名称
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
     * 写出顺序 数值越小约优先写出
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

    /**
     * 时间格式
     * 默认:  {@link SimpleCsvTypeConverter}
     * <p>
     * Date: yyyy-MM-dd HH:mm:ss
     * LocalDate: yyyy-MM-dd
     * LocalTime: HH:mm:ss
     * LocalDateTime: yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    String pattern() default "";


}

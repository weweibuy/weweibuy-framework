package com.weweibuy.framework.common.mvc.date;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.model.eum.DateTypeEum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间戳日期
 *
 * @author durenhao
 * @date 2019/9/13 15:55
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = LongDateSerialize.class)
@JacksonAnnotationsInside
public @interface LongData {

    /**
     * 日期格式
     *
     * @return
     */
    String format() default CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMAT_STR;

    DateTypeEum dateType() default DateTypeEum.DATETIME;


}

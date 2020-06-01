package com.weweibuy.framework.common.mvc.desensitization;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Jackson 响应报文脱敏
 *
 * @author durenhao
 * @date 2019/9/13 15:55
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = SensitiveInfoSerialize.class)
@JacksonAnnotationsInside
public @interface SensitiveData {

    /**
     * 替换字符
     *
     * @return
     */
    String replace();

    /**
     * 匹配正则
     *
     * @return
     */
    String patten();


}

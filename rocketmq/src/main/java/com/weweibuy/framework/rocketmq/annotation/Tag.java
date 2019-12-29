package com.weweibuy.framework.rocketmq.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生产消息时标识 Tag
 *
 *
 * @author durenhao
 * @date 2019/12/29 22:40
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag {


}

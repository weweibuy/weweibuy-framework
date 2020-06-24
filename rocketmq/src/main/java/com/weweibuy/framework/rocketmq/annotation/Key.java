package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.support.PropertyParameterProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 生产消息时标识Key
 * <p>
 * 生产时被: {@link PropertyParameterProcessor} 处理,加入到消息的属性中
 *
 * @author durenhao
 * @date 2019/12/29 22:42
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {
}

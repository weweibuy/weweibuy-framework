package com.weweibuy.framework.rocketmq.annotation;

import com.weweibuy.framework.rocketmq.constant.MessageExtPropertyConstant;
import com.weweibuy.framework.rocketmq.core.consumer.PropertyMethodArgumentResolver;
import com.weweibuy.framework.rocketmq.support.PropertyParameterProcessor;
import org.apache.rocketmq.common.message.MessageConst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记参数为 RocketMq消息的头信息(属性)
 * <p>
 * 生产时被: {@link PropertyParameterProcessor} 处理,加入到消息的属性中,不能被 {@link MessageConst#STRING_HASH_SET}包含
 * 消费时被 {@link PropertyMethodArgumentResolver} 解析,映射到方法的入参上,在解析是会将 MessageExt对象中
 *  {@link MessageExtPropertyConstant#MESSAGE_EXT_PROPERTIES_SET} 中包含的属性,视为 Property
 *
 * @author durenhao
 * @date 2019/12/31 21:57
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * Property 属性名
     * 消息生产时禁止使用 {@link MessageExtPropertyConstant#MESSAGE_EXT_PROPERTIES_SET} 中包含的属性
     *
     * @return
     */
    String value() default "";


}

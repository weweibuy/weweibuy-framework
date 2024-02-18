package com.weweibuy.framework.biztask.annotation;

import com.weweibuy.framework.biztask.config.BizTaskBaseConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author durenhao
 * @date 2024/2/6 11:46
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({BizTaskBaseConfig.class, BizTaskBaseConfig.class})
public @interface EnableBizTask {
}

package com.weweibuy.framework.common.db.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定数据源
 *
 * @author durenhao
 * @date 2023/12/1 18:05
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecDatasource {

    /**
     * 数据源名称
     *
     * @return
     */
    String value();


}

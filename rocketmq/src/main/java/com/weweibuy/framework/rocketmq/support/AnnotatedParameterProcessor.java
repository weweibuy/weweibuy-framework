/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.support;

import java.lang.annotation.Annotation;

/**
 * 参数注解解析器
 *
 * @author durenhao
 * @date 2019/12/31 16:46
 **/
public interface AnnotatedParameterProcessor<T extends Annotation> {

    /**
     * 获取注解类型
     *
     * @return
     */
    T getAnnotationType();


}

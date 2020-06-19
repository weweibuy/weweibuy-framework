package com.weweibuy.framework.idempotent.core.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;


/**
 * @author durenhao
 * @date 2020/6/19 22:40
 **/
@Getter
@AllArgsConstructor
public class IdempotentExpressionRootObject {

    private final Object[] args;

    private final Method method;

    private final Object target;

    private final Class targetClass;

    public String getMethodName() {
        return method.getName();
    }

    public String getTargetClassName() {
        return targetClass.getName();
    }

}

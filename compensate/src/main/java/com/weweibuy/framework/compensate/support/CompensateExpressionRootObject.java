package com.weweibuy.framework.compensate.support;

import lombok.Getter;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/2/16 11:28
 **/
@Getter
public class CompensateExpressionRootObject {

    private final Object[] args;

    private final Method method;

    private final Object target;


    public CompensateExpressionRootObject(Object[] args, Method method, Object target) {
        this.args = args;
        this.method = method;
        this.target = target;
    }
}

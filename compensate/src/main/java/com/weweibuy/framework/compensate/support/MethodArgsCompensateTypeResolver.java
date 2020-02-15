package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.core.CompensateInfo;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/2/14 22:04
 **/
public class MethodArgsCompensateTypeResolver implements CompensateTypeResolver {

    private final MethodArgsWrapperConverter methodArgsWrapperConverter;

    public MethodArgsCompensateTypeResolver(MethodArgsWrapperConverter methodArgsWrapperConverter) {
        this.methodArgsWrapperConverter = methodArgsWrapperConverter;
    }


    @Override
    public boolean match(Integer compensateType) {
        return compensateType == 0;
    }

    @Override
    public CompensateInfo resolver(String key, Object target, Method method, Object[] args) {
        CompensateInfo compensateInfo = new CompensateInfo();
        compensateInfo.setCompensateKey(key);
        compensateInfo.setArgs(methodArgsWrapperConverter.convert(key, args));
        return compensateInfo;
    }

    @Override
    public Object[] deResolver(CompensateInfo compensateInfo) {
        return methodArgsWrapperConverter.parser(compensateInfo);
    }
}

package com.weweibuy.framework.compensate.support;


import com.weweibuy.framework.compensate.interfaces.annotation.Compensate;
import com.weweibuy.framework.compensate.interfaces.model.BuiltInCompensateType;
import com.weweibuy.framework.compensate.interfaces.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfo;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/2/14 22:04
 **/
public class MethodArgsCompensateTypeResolver extends AbstractCompensateTypeResolver {

    private final MethodArgsConverter methodArgsWrapperConverter;

    public MethodArgsCompensateTypeResolver(MethodArgsConverter methodArgsWrapperConverter) {
        this.methodArgsWrapperConverter = methodArgsWrapperConverter;
    }


    @Override
    public boolean match(String compensateType) {
        return BuiltInCompensateType.METHOD_ARGS.toString().equals(compensateType);
    }

    @Override
    public CompensateInfo.CompensateInfoBuilder resolverCustom(Compensate annotation, Object target, Method method, Object[] args, CompensateConfigProperties configProperties) {
        return CompensateInfo.builder().methodArgs(methodArgsWrapperConverter.convert(annotation.key(), args));
    }

    @Override
    public Object[] deResolver(CompensateInfo compensateInfo) {
        return methodArgsWrapperConverter.parser(compensateInfo);
    }


}

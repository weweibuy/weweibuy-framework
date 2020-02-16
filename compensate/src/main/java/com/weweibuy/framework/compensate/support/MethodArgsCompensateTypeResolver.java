package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.CompensateConfigProperties;
import com.weweibuy.framework.compensate.core.CompensateConfigStore;
import com.weweibuy.framework.compensate.core.CompensateInfo;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author durenhao
 * @date 2020/2/14 22:04
 **/
public class MethodArgsCompensateTypeResolver implements CompensateTypeResolver {

    private final MethodArgsConverter methodArgsWrapperConverter;

    private final CompensateConfigStore compensateConfigStore;

    public MethodArgsCompensateTypeResolver(MethodArgsConverter methodArgsWrapperConverter, CompensateConfigStore compensateConfigStore) {
        this.methodArgsWrapperConverter = methodArgsWrapperConverter;
        this.compensateConfigStore = compensateConfigStore;
    }


    @Override
    public boolean match(Integer compensateType) {
        return compensateType == 0;
    }

    @Override
    public CompensateInfo resolver(Compensate annotation, Object target, Method method, Object[] args) {
        CompensateInfo compensateInfo = new CompensateInfo();
        String key = annotation.key();
        compensateInfo.setCompensateKey(key);
        compensateInfo.setArgs(methodArgsWrapperConverter.convert(key, args));
        CompensateConfigProperties configProperties = compensateConfigStore.compensateConfig(key);
        long parser = RuleParser.parser(0, configProperties.getRetryRule());
        compensateInfo.setNextTriggerTime(LocalDateTime.now().plus(parser, ChronoUnit.MILLIS));
        return compensateInfo;
    }

    @Override
    public Object[] deResolver(CompensateInfo compensateInfo) {
        return methodArgsWrapperConverter.parser(compensateInfo);
    }



}

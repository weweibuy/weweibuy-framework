package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.common.core.support.ExecRuleParser;
import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.CompensateTypeResolver;
import com.weweibuy.framework.compensate.core.RuleParser;
import com.weweibuy.framework.compensate.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.model.CompensateInfo;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author durenhao
 * @date 2020/2/16 20:30
 **/
public abstract class AbstractCompensateTypeResolver implements CompensateTypeResolver {

    private CompensateCachedExpressionEvaluator expressionEvaluator = new CompensateCachedExpressionEvaluator();

    @Override
    public CompensateInfo resolver(Compensate annotation, Object target, Method method, Object[] args, CompensateConfigProperties configProperties) {
        CompensateInfo.CompensateInfoBuilder builder = resolverCustom(annotation, target, method, args, configProperties);
        return builder.compensateKey(annotation.key())
                .bizId(resolverBizId(annotation, target, method, args))
                .nextTriggerTime(
                        LocalDateTime.now().plus(
                                ExecRuleParser.parser(0, configProperties.getRetryRule()), ChronoUnit.MILLIS))
                .build();
    }


    public abstract CompensateInfo.CompensateInfoBuilder resolverCustom(Compensate annotation, Object target, Method method, Object[] args, CompensateConfigProperties configProperties);


    private String resolverBizId(Compensate annotation, Object target, Method method, Object[] args) {
        return expressionEvaluator.evaluatorBizId(annotation, target, method, args);
    }
}

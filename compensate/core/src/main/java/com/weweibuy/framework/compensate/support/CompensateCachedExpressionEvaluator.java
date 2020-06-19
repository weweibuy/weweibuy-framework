package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.common.core.expression.CommonCachedExpressionEvaluator;
import com.weweibuy.framework.compensate.interfaces.annotation.Compensate;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/2/16 11:23
 **/
public class CompensateCachedExpressionEvaluator extends CommonCachedExpressionEvaluator {


    public String evaluatorBizId(Compensate annotation, Object target, Method method, Object[] args) {
        if (StringUtils.isBlank(annotation.bizId())) {
            return StringUtils.EMPTY;
        }
        return evaluatorExpressionStr(annotation.bizId(), target, target.getClass(), method, args);
    }

    @Override
    protected Object getRootObject(Object target, Class clazz, Method method, Object[] args) {
        return new CompensateExpressionRootObject(args, method, target);
    }
}

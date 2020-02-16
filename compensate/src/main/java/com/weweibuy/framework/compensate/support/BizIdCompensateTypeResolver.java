package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.CompensateInfo;

import java.lang.reflect.Method;

/**
 * 根据业务ID 解析补偿信息
 *
 * @author durenhao
 * @date 2020/2/16 11:10
 **/
public class BizIdCompensateTypeResolver implements CompensateTypeResolver {

    private CompensateCachedExpressionEvaluator expressionEvaluator = new CompensateCachedExpressionEvaluator();

    private final BizIdCompensateAssemble bizIdCompensateAssemble;

    public BizIdCompensateTypeResolver(BizIdCompensateAssemble bizIdCompensateAssemble) {
        this.bizIdCompensateAssemble = bizIdCompensateAssemble;
    }

    @Override
    public boolean match(Integer compensateType) {
        return compensateType == 1;
    }

    @Override
    public CompensateInfo resolver(Compensate annotation, Object target, Method method, Object[] args) {
        String bizId = expressionEvaluator.evaluatorBizId(annotation, target, method, args);
        CompensateInfo compensateInfo = new CompensateInfo();
        compensateInfo.setBizId(bizId);
        compensateInfo.setCompensateKey(annotation.key());
        return compensateInfo;
    }

    @Override
    public Object[] deResolver(CompensateInfo compensateInfo) {
        String bizId = compensateInfo.getBizId();
        // 根据BizId 组装参数
        return bizIdCompensateAssemble.assemble(compensateInfo.getCompensateKey(), bizId);
    }
}

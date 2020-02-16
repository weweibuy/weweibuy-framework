package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.interfaces.BizIdCompensateAssemble;
import com.weweibuy.framework.compensate.interfaces.annotation.Compensate;
import com.weweibuy.framework.compensate.interfaces.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfo;

import java.lang.reflect.Method;

/**
 * 根据业务ID 解析补偿信息
 *
 * @author durenhao
 * @date 2020/2/16 11:10
 **/
public class BizIdCompensateTypeResolver extends AbstractCompensateTypeResolver {

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
    public CompensateInfo.CompensateInfoBuilder resolverCustom(Compensate annotation, Object target, Method method, Object[] args, CompensateConfigProperties configProperties) {
        return CompensateInfo.builder().bizId(expressionEvaluator.evaluatorBizId(annotation, target, method, args));
    }

    @Override
    public Object[] deResolver(CompensateInfo compensateInfo) {
        String bizId = compensateInfo.getBizId();
        // 根据BizId 组装参数
        return bizIdCompensateAssemble.assemble(compensateInfo.getCompensateKey(), bizId);
    }
}

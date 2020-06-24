package com.weweibuy.framework.compensate.support;


import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.BizIdCompensateAssemble;
import com.weweibuy.framework.compensate.model.BuiltInCompensateType;
import com.weweibuy.framework.compensate.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.model.CompensateInfo;

import java.lang.reflect.Method;

/**
 * 根据业务ID 解析补偿信息
 *
 * @author durenhao
 * @date 2020/2/16 11:10
 **/
public class BizIdCompensateTypeResolver extends AbstractCompensateTypeResolver {

    private final BizIdCompensateAssemble bizIdCompensateAssemble;

    public BizIdCompensateTypeResolver(BizIdCompensateAssemble bizIdCompensateAssemble) {
        this.bizIdCompensateAssemble = bizIdCompensateAssemble;
    }

    @Override
    public boolean match(String compensateType) {
        return BuiltInCompensateType.BIZ_ID.toString().equals(compensateType);
    }


    @Override
    public CompensateInfo.CompensateInfoBuilder resolverCustom(Compensate annotation, Object target, Method method, Object[] args, CompensateConfigProperties configProperties) {
        return CompensateInfo.builder();
    }

    @Override
    public Object[] deResolver(CompensateInfo compensateInfo) {
        String bizId = compensateInfo.getBizId();
        // 根据BizId 组装参数
        return bizIdCompensateAssemble.assemble(compensateInfo.getCompensateKey(), bizId);
    }
}

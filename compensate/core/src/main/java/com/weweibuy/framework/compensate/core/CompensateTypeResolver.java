package com.weweibuy.framework.compensate.core;


import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.model.CompensateConfigProperties;
import com.weweibuy.framework.compensate.model.CompensateInfo;

import java.lang.reflect.Method;

/**
 * 补仓类型解析器
 *
 * @author durenhao
 * @date 2020/2/14 19:32
 **/
public interface CompensateTypeResolver {

    /**
     * 是否匹配
     *
     * @param compensateType
     * @return
     */
    boolean match(String compensateType);

    /**
     * 解析补偿信息
     *
     * @param annotation
     * @param target
     * @param method
     * @param args
     * @param configProperties
     * @return
     */
    CompensateInfo resolver(Compensate annotation, Object target, Method method, Object[] args, CompensateConfigProperties configProperties);

    /**
     * 反解析补偿信息
     *
     * @param compensateInfo
     * @return
     */
    Object[] deResolver(CompensateInfo compensateInfo);
}

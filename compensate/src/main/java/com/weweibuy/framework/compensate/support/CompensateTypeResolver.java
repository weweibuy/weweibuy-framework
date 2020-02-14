package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.core.CompensateInfo;

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
    boolean match(Integer compensateType);

    /**
     * 解析补偿信息
     *
     * @param key
     * @param target
     * @param method
     * @param args
     * @return
     */
    CompensateInfo resolver(String key, Object target, Method method, Object[] args);

    /**
     * 反解析补偿信息
     *
     * @param compensateInfo
     * @return
     */
    Object[] deResolver(CompensateInfo compensateInfo);
}

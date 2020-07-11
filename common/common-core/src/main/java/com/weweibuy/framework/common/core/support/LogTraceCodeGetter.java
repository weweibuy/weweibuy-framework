package com.weweibuy.framework.common.core.support;

/**
 * 获取 traceCode 与 UserCode
 *
 * @author durenhao
 * @date 2020/7/10 16:13
 **/
public interface LogTraceCodeGetter<T> {

    /**
     * 获取 TraceCode
     *
     * @return
     */
    String getTraceCode(T t);

    /**
     * 获取 userCode
     *
     * @return
     */
    String getUserCode(T t);

}

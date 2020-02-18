package com.weweibuy.framework.rocketmq.core.provider;

/**
 * @author durenhao
 * @date 2019/12/29 23:12
 **/
public interface MethodHandler {

    Object invoke(Object[] arg) throws Throwable;


}

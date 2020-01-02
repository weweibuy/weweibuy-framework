package com.weweibuy.framework.rocketmq.support.provider;

/**
 * @author durenhao
 * @date 2020/1/2 17:48
 **/
public abstract class AbstractLinkedMessagePostProcessor implements MessagePostProcessor {



    /**
     * 获取执行顺序
     *
     * @return
     */
    public abstract Integer getOrder();


}

package com.weweibuy.framework.rocketmq.support;

import org.apache.rocketmq.client.producer.MQProducer;

/**
 * 代理的rocketMQ 生产者
 *
 * @author durenhao
 * @date 2019/12/29 23:17
 **/
public class ProxyRocketProvider {

    public Object newInstance(MQProducer mqProducer) {

        return "";
    }


    /**
     * 解析 methodHandler
     */
    private static class ParseHandlersByName {

    }


}

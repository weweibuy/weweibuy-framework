/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.rocketmq.core.consumer;

import java.util.Map;

/**
 * 抽象监听容器
 *
 * @author durenhao
 * @date 2020/1/8 11:54
 **/
public abstract class AbstractRocketListenerContainer<T, R> implements RocketListenerContainer<T, R> {

    @Override
    public RocketMessageListener selectMessageListener(String tag, Map map) {
        return null;
    }

}

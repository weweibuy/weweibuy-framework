package com.weweibuy.framework.rocketmq.core;

import com.weweibuy.framework.rocketmq.support.ProxyRocketProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author durenhao
 * @date 2019/12/29 10:35
 **/
@Slf4j
@Setter
public class RocketProviderFactoryBean implements FactoryBean<Object> {

    private Class<?> type;

    private String name;

    private String topic;

    private ProxyRocketProvider proxyRocketProvider;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, proxyRocketProvider.newInstance(type));
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }


}

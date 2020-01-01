package com.weweibuy.framework.rocketmq.core;

import com.weweibuy.framework.rocketmq.support.ProxyRocketProvider;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

/**
 * @author durenhao
 * @date 2019/12/29 10:35
 **/
@Slf4j
@Setter
public class RocketProviderFactoryBean implements FactoryBean<Object>, ApplicationContextAware {

    private Class<?> type;

    private String name;

    private String topic;

    private ProxyRocketProvider proxyRocketProvider;

    private ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {

        ProxyRocketProvider bean = applicationContext.getBean(ProxyRocketProvider.class);

        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, bean.newInstance(type));
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

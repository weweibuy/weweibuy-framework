package com.weweibuy.framework.rocketmq.core.provider;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;

/**
 * 构建 RocketProvider  代理对象
 *
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
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

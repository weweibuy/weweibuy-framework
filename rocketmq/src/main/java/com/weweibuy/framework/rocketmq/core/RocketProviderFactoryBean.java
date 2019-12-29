package com.weweibuy.framework.rocketmq.core;

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


    @Override
    public Object getObject() throws Exception {
        return  Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type},
                (a, b, c) -> {
                    log.info("进入代理方法!!! 方法名: {}, 参数:{}", b.getName(), c);
                    return b.invoke(c);
                });
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }


}

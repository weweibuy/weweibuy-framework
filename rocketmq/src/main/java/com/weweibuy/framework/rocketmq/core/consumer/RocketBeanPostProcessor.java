package com.weweibuy.framework.rocketmq.core.consumer;

import com.weweibuy.framework.rocketmq.annotation.RocketListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/1/4 20:31
 **/
public class RocketBeanPostProcessor implements BeanPostProcessor, SmartInitializingSingleton {

    private Map<String, RocketListener> listenerMap = new HashMap<>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        RocketListener listenerAnnotation = findListenerAnnotation(bean.getClass());
        if(listenerAnnotation != null){
            listenerMap.put(beanName, listenerAnnotation);
        }

        return bean;
    }



    private RocketListener findListenerAnnotation(Class<?> clazz) {
        return AnnotationUtils.findAnnotation(clazz, RocketListener.class);
    }



    @Override
    public void afterSingletonsInstantiated() {
        System.err.println(listenerMap.size());
    }
}

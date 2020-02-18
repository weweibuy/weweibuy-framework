package com.weweibuy.framework.rocketmq.core;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.Set;

/**
 * bean 扫描注册器
 *
 * @author durenhao
 * @date 2019/12/28 23:06
 **/
public class ClassPathRocketScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathRocketScanner(BeanDefinitionRegistry registry, Environment environment, ResourceLoader resourceLoader) {
        super(registry, false, environment, resourceLoader);
    }


    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        boolean isCandidate = false;
        if (beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation() &&
                beanDefinition.getMetadata().isInterface()) {
            isCandidate = true;
        }
        return isCandidate;
    }


}
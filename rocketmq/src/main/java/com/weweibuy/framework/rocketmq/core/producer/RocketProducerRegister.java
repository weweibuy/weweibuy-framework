package com.weweibuy.framework.rocketmq.core.producer;

import com.weweibuy.framework.rocketmq.annotation.EnableRocketProducer;
import com.weweibuy.framework.rocketmq.annotation.RocketProducer;
import com.weweibuy.framework.rocketmq.core.ClassPathRocketScanner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.*;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 注册rocketMQ Provider
 *
 * @author durenhao
 * @date 2019/12/28 22:01
 **/
public class RocketProducerRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

//    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

     // spring 5.2 之后
     private BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;

    private ResourceLoader resourceLoader;

    private Environment environment;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                RocketProducer.class);
        ClassPathRocketScanner classPathRocketScanner = new ClassPathRocketScanner(registry, environment, resourceLoader);
        classPathRocketScanner.addIncludeFilter(annotationTypeFilter);

        Set<String> basePackages = getBasePackages(metadata);

        basePackages.stream()
                .map(classPathRocketScanner::findCandidateComponents)
                .flatMap(Set::stream)
                .forEach(d -> registryRocket(registry, d));

    }

    private void registryRocket(BeanDefinitionRegistry registry, BeanDefinition beanDefinition) {
        if (beanDefinition instanceof AnnotatedBeanDefinition) {
            AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
            AnnotationMetadata annotationMetadata = annotatedBeanDefinition.getMetadata();

            Map<String, Object> attributes = annotationMetadata
                    .getAnnotationAttributes(RocketProducer.class.getCanonicalName());

            validateAnnotationMetadata(attributes);
            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder
                    .genericBeanDefinition(RocketProducerFactoryBean.class)
                    .addPropertyValue("name", beanName)
                    .addPropertyValue("type", annotatedBeanDefinition.getBeanClassName())
                    .addPropertyValue("topic", attributes.get("topic"))
//                    .setPrimary(true)  spring 5.1.1 之后才有
                    .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            AbstractBeanDefinition definition = definitionBuilder.getBeanDefinition();

            BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, beanName);
            BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        }
    }


    private void validateAnnotationMetadata(Map<String, Object> attributes) {
        String topic = (String) attributes.get("topic");
        Assert.hasText(topic, "@RocketProvider 属性 topic 或 group 必须有正确的值");
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableRocketProducer.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.isNotBlank(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.isNotBlank(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

package com.weweibuy.framework.compensate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.compensate.core.*;
import com.weweibuy.framework.compensate.interceptor.CompensateBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.compensate.interceptor.CompensateInterceptor;
import com.weweibuy.framework.compensate.interceptor.CompensatePointcut;
import com.weweibuy.framework.compensate.support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/2/13 21:24
 **/
@Configuration
public class CompensateAutoConfig extends AbstractCompensateConfig {

    @Autowired(required = false)
    private List<CompensateTypeResolver> compensateTypeResolverList;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MethodArgsTypeHolder methodArgsTypeHolder;

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CompensateBeanFactoryPointcutAdvisor compensateBeanFactoryPointcutAdvisor(CompensateAnnotationMetaDataParser parser,
                                                                                     CompensateStore store) {
        CompensateBeanFactoryPointcutAdvisor advisor = new CompensateBeanFactoryPointcutAdvisor();
        advisor.setPc(new CompensatePointcut());
        advisor.setOrder(getEnableCompensate().<Integer>getNumber("order"));
        advisor.setAdvice(new CompensateInterceptor(store, parser));
        return advisor;
    }

    @Bean
    public CompensateAnnotationMetaDataParser metaDataParser(CompensateConfigStore compensateConfigStore, CompensateTypeResolverComposite composite) {
        return new CompensateAnnotationMetaDataParser(compensateConfigStore, composite);
    }

    @Bean
    @ConditionalOnMissingBean(CompensateConfigStore.class)
    public CompensateConfigStore compensateConfigStore() {
        return new SimpleCompensateConfigStore();
    }

    @Bean
    @ConditionalOnMissingBean(CompensateStore.class)
    public CompensateStore compensateStore() {
        return new SimpleCompensateStore();
    }

    @Bean
    public CompensateTypeResolverComposite compensateTypeResolverComposite(MethodArgsWrapperConverter converter) {
        CompensateTypeResolverComposite composite = new CompensateTypeResolverComposite();
        composite.addResolver(new MethodArgsCompensateTypeResolver(converter));
        if (compensateTypeResolverList != null) {
            composite.addResolvers(compensateTypeResolverList);
        }
        return composite;
    }

    @Bean
    public MethodArgsWrapperConverter jsonMethodArgsWrapperConverter(ObjectMapper objectMapper) {
        return new JackJsonMethodArgsWrapperConverter(objectMapper, methodArgsTypeHolder);
    }

    @Bean
    public MethodArgsTypeHolder methodArgsTypeHolder(ObjectMapper objectMapper) {
        return new MethodArgsTypeHolder(objectMapper);
    }

    @Bean
    public CompensateBeanPostProcessor compensateBeanPostProcessor(CompensateMethodRegister register) {
        CompensateBeanPostProcessor compensateBeanPostProcessor = new CompensateBeanPostProcessor(register);
        compensateBeanPostProcessor.setMethodArgsTypeHolder(methodArgsTypeHolder);
        return compensateBeanPostProcessor;
    }

    @Bean
    public CompensateMethodRegister compensateMethodRegister() {
        return new CompensateMethodRegister(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean(CompensateTrigger.class)
    public CompensateTrigger simpleCompensateTrigger(CompensateStore compensateStore, CompensateHandlerService service) {
        return new SimpleCompensateTrigger(compensateStore, service);
    }

    @Bean
    public CompensateHandlerService compensateHandlerService(CompensateMethodRegister compensateMethodRegister,
                                                             CompensateConfigStore compensateConfigStore, CompensateStore compensateStore,
                                                             CompensateTypeResolverComposite composite) {
        return new CompensateHandlerService(compensateMethodRegister, compensateConfigStore, compensateStore, composite);
    }

}

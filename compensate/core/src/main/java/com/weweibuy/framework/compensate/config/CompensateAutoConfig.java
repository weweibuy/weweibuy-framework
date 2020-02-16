package com.weweibuy.framework.compensate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.compensate.core.*;
import com.weweibuy.framework.compensate.interceptor.CompensateBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.compensate.interceptor.CompensateInterceptor;
import com.weweibuy.framework.compensate.interceptor.CompensatePointcut;
import com.weweibuy.framework.compensate.interfaces.*;
import com.weweibuy.framework.compensate.support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

    @Autowired(required = false)
    private List<RecoverMethodArgsResolver> recoverMethodArgsResolverList;

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
    public CompensateTypeResolverComposite compensateTypeResolverComposite(MethodArgsConverter converter) {
        CompensateTypeResolverComposite composite = new CompensateTypeResolverComposite();
        composite.addResolver(new MethodArgsCompensateTypeResolver(converter));
        if (compensateTypeResolverList != null) {
            composite.addResolvers(compensateTypeResolverList);
        }
        return composite;
    }

    @Bean
    @ConditionalOnBean(BizIdCompensateAssemble.class)
    public CompensateTypeResolver bizIdCompensateTypeResolver(BizIdCompensateAssemble assemble) {
        return new BizIdCompensateTypeResolver(assemble);
    }

    @Bean
    public MethodArgsConverter jsonMethodArgsWrapperConverter(ObjectMapper objectMapper) {
        return new JackJsonMethodConverter(objectMapper, methodArgsTypeHolder);
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
    public CompensateMethodRegister compensateMethodRegister(RecoverMethodArgsResolverComposite composite) {
        return new CompensateMethodRegister(applicationContext, composite);
    }

    @Bean
    @ConditionalOnMissingBean(CompensateTrigger.class)
    public CompensateTrigger simpleCompensateTrigger(CompensateStore compensateStore, CompensateHandlerService service) {
        return new SimpleCompensateTrigger(compensateStore, service);
    }

    @Bean
    public CompensateHandlerService compensateHandlerService(CompensateMethodRegister compensateMethodRegister, CompensateStore compensateStore,
                                                             CompensateTypeResolverComposite composite, CompensateAlarmService alarmService) {
        return new CompensateHandlerService(compensateMethodRegister, compensateStore, composite, alarmService);
    }

    @Bean
    @ConditionalOnMissingBean(CompensateAlarmService.class)
    public CompensateAlarmService compensateAlarmService() {
        return new LogCompensateAlarmService();
    }

    @Bean
    public RecoverMethodArgsResolverComposite recoverMethodArgsResolverComposite() {
        RecoverMethodArgsResolverComposite composite = new RecoverMethodArgsResolverComposite();
        if (recoverMethodArgsResolverList != null) {
            composite.addResolvers(recoverMethodArgsResolverList);
        }
        composite.addResolver(new AppendArgsRecoverMethodArgsResolver());
        return composite;
    }

}

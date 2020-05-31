package com.weweibuy.framework.compensate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.compensate.core.*;
import com.weweibuy.framework.compensate.interfaces.*;
import com.weweibuy.framework.compensate.support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * @author durenhao
 * @date 2020/2/17 20:20
 **/
@Configuration
public class CompensateConfigurationSupport {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MethodArgsTypeHolder methodArgsTypeHolder;

    @Autowired(required = false)
    private List<CompensateConfigurer> configurerList;


    @Bean
    public CompensateAnnotationMetaDataParser metaDataParser(CompensateConfigStore compensateConfigStore,
                                                             MethodArgsConverter argsConverter, CompensateTypeResolverComposite composite) {


        return new CompensateAnnotationMetaDataParser(compensateConfigStore, composite);
    }

    @Bean
    @ConditionalOnMissingBean(CompensateConfigStore.class)
    public CompensateConfigStore compensateConfigStore() {
        return new SimpleCompensateConfigStore();
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
    public CompensateMethodRegister compensateMethodRegister() {

        RecoverMethodArgsResolverComposite composite = new RecoverMethodArgsResolverComposite();
        if (!CollectionUtils.isEmpty(configurerList)) {
            configurerList.forEach(c -> c.addRecoverMethodArgsResolver(composite));
        }
        composite.addResolver(new AppendArgsRecoverMethodArgsResolver());
        return new CompensateMethodRegister(applicationContext, composite);
    }

    @Bean
    @ConditionalOnMissingBean(CompensateTrigger.class)
    public CompensateTrigger simpleCompensateTrigger() {
        return new SimpleTimerCompensateTrigger();
    }

    @Bean
    public CompensateHandlerService compensateHandlerService(CompensateMethodRegister compensateMethodRegister, CompensateStore compensateStore,
                                                             CompensateTypeResolverComposite composite, CompensateAlarmService alarmService) {
        ExecutorService executorService = null;
        if (!CollectionUtils.isEmpty(configurerList)) {
            executorService = configurerList.stream().map(c -> c.getCompensateExecutorService())
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);
        }
        return new CompensateHandlerService(compensateMethodRegister, compensateStore, composite, alarmService, executorService);
    }

    @Bean
    @ConditionalOnMissingBean(CompensateAlarmService.class)
    public CompensateAlarmService compensateAlarmService() {
        return new LogCompensateAlarmService();
    }


    @Bean
    public CompensateTypeResolverComposite compensateTypeResolverComposite(MethodArgsConverter argsConverter) {
        CompensateTypeResolverComposite composite = new CompensateTypeResolverComposite();
        composite.addResolver(new MethodArgsCompensateTypeResolver(argsConverter));
        if (!CollectionUtils.isEmpty(configurerList)) {
            configurerList.forEach(c -> c.addCompensateTypeResolver(composite));
        }
        return composite;
    }


}

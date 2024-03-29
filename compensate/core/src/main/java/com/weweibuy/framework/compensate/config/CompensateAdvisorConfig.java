package com.weweibuy.framework.compensate.config;

import com.weweibuy.framework.compensate.annotation.EnableCompensate;
import com.weweibuy.framework.compensate.core.CompensateAlarmService;
import com.weweibuy.framework.compensate.core.CompensateStore;
import com.weweibuy.framework.compensate.interceptor.CompensateBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.compensate.interceptor.CompensateInterceptor;
import com.weweibuy.framework.compensate.interceptor.CompensatePointcut;
import com.weweibuy.framework.compensate.support.CompensateAnnotationMetaDataParser;
import com.weweibuy.framework.compensate.support.CompensateRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * 补偿切面配置
 *
 * @author durenhao
 * @date 2020/2/21 23:14
 **/
@Configuration
public class CompensateAdvisorConfig implements ImportAware {

    @Nullable
    protected AnnotationAttributes enableCompensate;

    @Autowired(required = false)
    private List<CompensateConfigurer> configurerList;

    @Autowired
    private CompensateAlarmService compensateAlarmService;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableCompensate = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableCompensate.class.getName(), false));
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public CompensateBeanFactoryPointcutAdvisor compensateBeanFactoryPointcutAdvisor(CompensateAnnotationMetaDataParser parser,
                                                                                     CompensateStore store, CompensateRecorder compensateRecorder) {
        ExecutorService executorService = null;
        if (!CollectionUtils.isEmpty(configurerList)) {
            executorService = configurerList.stream().map(CompensateConfigurer::getAdviceExecutorService)
                    .filter(Objects::nonNull)
                    .findFirst().orElse(null);
        }

        CompensateBeanFactoryPointcutAdvisor advisor = new CompensateBeanFactoryPointcutAdvisor();
        advisor.setPc(new CompensatePointcut());
        advisor.setOrder(enableCompensate.<Integer>getNumber("order"));
        advisor.setAdvice(new CompensateInterceptor(store, parser, executorService, compensateAlarmService, compensateRecorder));
        return advisor;
    }

}

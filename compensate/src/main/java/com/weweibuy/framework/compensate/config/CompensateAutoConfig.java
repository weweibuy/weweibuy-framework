package com.weweibuy.framework.compensate.config;

import com.weweibuy.framework.compensate.interceptor.CompensateBeanFactoryPointcutAdvisor;
import com.weweibuy.framework.compensate.interceptor.CompensateInterceptor;
import com.weweibuy.framework.compensate.interceptor.CompensatePointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author durenhao
 * @date 2020/2/13 21:24
 **/
@Configuration
public class CompensateAutoConfig extends AbstractCompensateConfig {

    @Bean
    public CompensateBeanFactoryPointcutAdvisor compensateBeanFactoryPointcutAdvisor() {
        CompensateBeanFactoryPointcutAdvisor advisor = new CompensateBeanFactoryPointcutAdvisor();
        advisor.setPc(new CompensatePointcut());
        advisor.setOrder(getEnableCompensate().<Integer>getNumber("order"));
        advisor.setAdvice(new CompensateInterceptor());
        return advisor;
    }


}

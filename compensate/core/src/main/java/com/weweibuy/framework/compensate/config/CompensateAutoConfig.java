package com.weweibuy.framework.compensate.config;

import com.weweibuy.framework.compensate.support.CompensateTypeResolverComposite;
import com.weweibuy.framework.compensate.support.RecoverMethodArgsResolverComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author durenhao
 * @date 2020/2/13 21:24
 **/
@Configuration
public class CompensateAutoConfig extends CompensateConfigurationSupport {

    private CompensateConfigurerComposite delegates = new CompensateConfigurerComposite();

    @Autowired(required = false)
    public void setConfigurers(List<CompensateConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addCompensateConfigurer(configurers);
        }
    }

    @Override
    protected ExecutorService getAdviceExecutorService() {
        return delegates.getAdviceExecutorService();
    }

    @Override
    protected ExecutorService getCompensateExecutorService() {
        return delegates.getCompensateExecutorService();
    }

    @Override
    protected void configAsyncSupport(CompensateAsyncSupportConfigurer configurer) {
    }

    @Override
    protected void configCompensateTypeResolver(CompensateTypeResolverComposite composite) {
        delegates.addCompensateTypeResolver(composite);
    }

    @Override
    protected void configRecoverMethodArgsResolver(RecoverMethodArgsResolverComposite composite) {
        delegates.addRecoverMethodArgsResolver(composite);
    }
}

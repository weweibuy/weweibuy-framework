package com.weweibuy.framework.compensate.config;

import com.weweibuy.framework.compensate.support.CompensateTypeResolverComposite;
import com.weweibuy.framework.compensate.support.RecoverMethodArgsResolverComposite;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * @author durenhao
 * @date 2020/2/17 20:41
 **/
public class CompensateConfigurerComposite implements CompensateConfigurer {

    private final List<CompensateConfigurer> delegates = new ArrayList<>();

    public void addCompensateConfigurer(List<CompensateConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addAll(configurers);
        }
    }


    @Override
    public ExecutorService getAdviceExecutorService() {
        return delegates.stream().map(d -> d.getAdviceExecutorService())
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    @Override
    public ExecutorService getCompensateExecutorService() {
        return delegates.stream().map(d -> d.getCompensateExecutorService())
                .filter(Objects::nonNull)
                .findFirst().orElse(null);

    }

    @Override
    public void addCompensateTypeResolver(CompensateTypeResolverComposite handlers) {
        delegates.forEach(d -> d.addCompensateTypeResolver(handlers));
    }

    @Override
    public void addRecoverMethodArgsResolver(RecoverMethodArgsResolverComposite handlers) {
        delegates.forEach(d -> d.addRecoverMethodArgsResolver(handlers));
    }

    @Override
    public CompensateAsyncSupportConfigurer getCompensateAsyncSupportConfigurer() {
        return delegates.stream().map(c ->
                c.getCompensateAsyncSupportConfigurer()).findFirst().orElse(null);
    }


}

package com.weweibuy.framework.compensate.config;

import com.weweibuy.framework.compensate.support.CompensateTypeResolverComposite;
import com.weweibuy.framework.compensate.support.RecoverMethodArgsResolverComposite;

import java.util.concurrent.ExecutorService;

/**
 * 补偿配置
 *
 * @author durenhao
 * @date 2020/2/17 20:21
 **/
public interface CompensateConfigurer {


    default ExecutorService getAdviceExecutorService() {
        return null;
    }

    default ExecutorService getCompensateExecutorService() {
        return null;
    }

    default void addCompensateTypeResolver(CompensateTypeResolverComposite composite) {
    }

    default void addRecoverMethodArgsResolver(RecoverMethodArgsResolverComposite composite) {
    }

    default CompensateAsyncSupportConfigurer getCompensateAsyncSupportConfigurer() {
        return null;
    }

}

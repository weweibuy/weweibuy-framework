package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.support.CompensateTypeResolverComposite;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2020/2/15 13:45
 **/
public class CompensateHandlerService {

    private ExecutorService executorService;

    private CompensateMethodRegister compensateMethodRegister;

    private CompensateConfigStore compensateConfigStore;

    private CompensateStore compensateStore;

    private CompensateTypeResolverComposite composite;

    public CompensateHandlerService(CompensateMethodRegister compensateMethodRegister, CompensateConfigStore compensateConfigStore,
                                    CompensateStore compensateStore, CompensateTypeResolverComposite composite) {
        this.compensateMethodRegister = compensateMethodRegister;
        this.compensateConfigStore = compensateConfigStore;
        this.compensateStore = compensateStore;
        this.composite = composite;
        this.executorService = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(100), new CustomizableThreadFactory("compensate-thread-"), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public CompensateHandlerService(ExecutorService executorService, CompensateMethodRegister compensateMethodRegister, CompensateConfigStore compensateConfigStore,
                                    CompensateStore compensateStore, CompensateTypeResolverComposite composite) {
        this.executorService = executorService;
        this.compensateMethodRegister = compensateMethodRegister;
        this.compensateConfigStore = compensateConfigStore;
        this.compensateStore = compensateStore;
        this.composite = composite;
    }

    public void compensate(Collection<CompensateInfo> compensateInfoCollection) {
        compensateInfoCollection.forEach(c -> executorService.execute(() -> compensate(c)));
    }

    public void compensate(CompensateInfo compensateInfo) {
        CompensateConfigProperties configProperties = compensateConfigStore.compensateConfig(compensateInfo.getCompensateKey());
        Integer compensateType = configProperties.getCompensateType();

        Object[] objects = composite.getArgumentResolver(compensateType).deResolver(compensateInfo);
        CompensateHandlerMethod compensateHandlerMethod = compensateMethodRegister.getCompensateHandlerMethod(compensateInfo.getCompensateKey());
        Object invoke = null;
        try {
            invoke = compensateHandlerMethod.invoke(objects);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }

        if (compensateHandlerMethod.hasRecoverMethod()) {
            // 转为Recover 方法参数

            try {
                compensateHandlerMethod.invokeRecover(null);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
        compensateStore.deleteCompensateInfo("1");

    }

}

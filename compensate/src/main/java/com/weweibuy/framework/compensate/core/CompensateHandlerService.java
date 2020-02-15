package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.support.CompensateAlarmService;
import com.weweibuy.framework.compensate.support.CompensateTypeResolverComposite;
import com.weweibuy.framework.compensate.support.LogCompensateAlarmService;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author durenhao
 * @date 2020/2/15 13:45
 **/
public class CompensateHandlerService {

    private ExecutorService executorService;

    private CompensateMethodRegister compensateMethodRegister;

    private CompensateStore compensateStore;

    private CompensateTypeResolverComposite composite;

    private CompensateAlarmService compensateAlarmService;

    public CompensateHandlerService(CompensateMethodRegister compensateMethodRegister,
                                    CompensateStore compensateStore, CompensateTypeResolverComposite composite) {
        this.compensateMethodRegister = compensateMethodRegister;
        this.compensateStore = compensateStore;
        this.composite = composite;
        this.executorService = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100), new CustomizableThreadFactory("compensate-thread-"), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public CompensateHandlerService(ExecutorService executorService, CompensateMethodRegister compensateMethodRegister,
                                    CompensateStore compensateStore, CompensateTypeResolverComposite composite) {
        this.executorService = executorService;
        this.compensateMethodRegister = compensateMethodRegister;
        this.compensateStore = compensateStore;
        this.composite = composite;
        compensateAlarmService = new LogCompensateAlarmService();
    }

    public void compensate(Collection<CompensateInfoExt> compensateInfoCollection) {
        compensateInfoCollection.forEach(c -> executorService.execute(() -> compensate(c)));
    }

    public void compensate(CompensateInfoExt compensateInfo) {
        // 判断是否重试
        if (!compensateInfo.shouldCompensate()) {
            if (compensateInfo.shouldAlarm()) {
                compensateAlarmService.sendAlarm(compensateInfo);
                // 更新
                compensateStore.updateCompensateInfo(compensateInfo.getId(), compensateInfo.addAlarmToCompensateInfo());
                return;
            }
        }
        // 判读是否报警
        Object[] objects = composite.getArgumentResolver(compensateInfo.getType()).deResolver(compensateInfo);
        CompensateHandlerMethod compensateHandlerMethod = compensateMethodRegister.getCompensateHandlerMethod(compensateInfo.getCompensateKey());
        Object invoke = null;
        try {
            invoke = compensateHandlerMethod.invoke(objects);
            if (compensateHandlerMethod.hasRecoverMethod()) {
                // 转为Recover 方法参数
                compensateHandlerMethod.invokeRecover(null);
            }
        } catch (InvocationTargetException e) {
            compensateStore.updateCompensateInfo(compensateInfo.getId(), compensateInfo.addRetryToCompensateInfo());
            // 更新重试次数
            throw new RuntimeException(e);
        }
        compensateStore.deleteCompensateInfo(compensateInfo.getId());
    }

}

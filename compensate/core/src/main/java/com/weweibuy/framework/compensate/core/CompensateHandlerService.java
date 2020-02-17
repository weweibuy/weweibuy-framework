package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.exception.CompensateException;
import com.weweibuy.framework.compensate.interfaces.CompensateAlarmService;
import com.weweibuy.framework.compensate.interfaces.CompensateStore;
import com.weweibuy.framework.compensate.interfaces.RecoverMethodArgsResolver;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfoExt;
import com.weweibuy.framework.compensate.support.CompensateTypeResolverComposite;
import com.weweibuy.framework.compensate.support.LogCompensateAlarmService;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * 补偿处理服务
 *
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
                                    CompensateStore compensateStore, CompensateTypeResolverComposite composite,
                                    CompensateAlarmService compensateAlarmService, ExecutorService executorService) {
        this.compensateMethodRegister = compensateMethodRegister;
        this.compensateStore = compensateStore;
        this.composite = composite;
        this.compensateAlarmService = compensateAlarmService;
        this.executorService = executorService;
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
        if (executorService != null) {
            compensateInfoCollection.forEach(c -> executorService.execute(() -> compensate(c)));
        } else {
            compensateInfoCollection.forEach(this::compensate);
        }
    }

    public void compensate(CompensateInfoExt compensateInfo) {
        // 补偿状态
        CompensateInfoExt.CompensateStatus compensateStatus = RuleParser.parserToStatus(compensateInfo);

        switch (compensateStatus) {
            case RETRY_ABLE:
                execCompensate(compensateInfo);
                return;
            case ALARM_ABLE:
                execAlarm(compensateInfo);
                return;
            case OVER_ALARM_COUNT:
                compensateStore.deleteCompensateInfo(compensateInfo.getId());
                return;
            default:
                return;
        }
    }


    private void execAlarm(CompensateInfoExt compensateInfo) {
        compensateAlarmService.sendAlarm(compensateInfo);
        compensateStore.updateCompensateInfo(compensateInfo.getId(),
                compensateInfo.addAlarmToCompensateInfo(RuleParser.nextTriggerTime(compensateInfo)));
    }


    private void execCompensate(CompensateInfoExt compensateInfo) {
        try {
            doCompensate(compensateInfo);
        } catch (Exception e) {
            compensateStore.updateCompensateInfo(compensateInfo.getId(),
                    compensateInfo.addRetryToCompensateInfo(RuleParser.nextTriggerTime(compensateInfo)));
            throw new CompensateException(e);
        }
        compensateStore.deleteCompensateInfo(compensateInfo.getId());
    }

    private void doCompensate(CompensateInfoExt compensateInfo) throws InvocationTargetException {
        Object[] objects = composite.getArgumentResolver(compensateInfo.getType()).deResolver(compensateInfo);
        CompensateHandlerMethod compensateHandlerMethod = compensateMethodRegister.getCompensateHandlerMethod(compensateInfo.getCompensateKey());
        Object invoke = compensateHandlerMethod.invoke(objects);
        if (compensateHandlerMethod.hasRecoverMethod()) {
            RecoverMethodArgsResolver argumentResolver = compensateHandlerMethod.getComposite().getArgumentResolver(compensateInfo.getCompensateKey());
            Object[] resolver = argumentResolver.resolver(invoke, objects);
            // 转为Recover 方法参数
            compensateHandlerMethod.invokeRecover(resolver);
        }
    }

}

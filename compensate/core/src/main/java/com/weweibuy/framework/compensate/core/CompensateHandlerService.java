package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.compensate.exception.CompensateException;
import com.weweibuy.framework.compensate.interfaces.CompensateAlarmService;
import com.weweibuy.framework.compensate.interfaces.CompensateStore;
import com.weweibuy.framework.compensate.interfaces.RecoverMethodArgsResolver;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfoExt;
import com.weweibuy.framework.compensate.interfaces.model.CompensateResult;
import com.weweibuy.framework.compensate.interfaces.model.CompensateResultEum;
import com.weweibuy.framework.compensate.support.CompensateTypeResolverComposite;
import com.weweibuy.framework.compensate.support.LogCompensateAlarmService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 补偿处理服务
 *
 * @author durenhao
 * @date 2020/2/15 13:45
 **/
@Slf4j
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

    /**
     * 进行补偿
     *
     * @param compensateInfoCollection
     */
    public List<CompensateResult> compensate(Collection<CompensateInfoExt> compensateInfoCollection) {
        if (executorService != null) {
            return compensateConcurrently(compensateInfoCollection);
        } else {
            return compensateInfoCollection.stream()
                    .map(c -> compensate(c, false))
                    .collect(Collectors.toList());
        }
    }


    private List<CompensateResult> compensateConcurrently(Collection<CompensateInfoExt> compensateInfoCollection) {
        List<Future<CompensateResult>> futureList = compensateInfoCollection.stream()
                .map(c -> executorService.submit(() -> compensate(c, false)))
                .collect(Collectors.toList());
        return futureList.stream().map(f -> {
            try {
                return f.get(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new CompensateException(e);
            }
        }).collect(Collectors.toList());
    }


    /**
     * 强制补偿  忽略重试,报警,触发时间 直接进行补偿
     *
     * @param compensateInfoCollection
     */
    public List<CompensateResult> compensateForce(Collection<CompensateInfoExt> compensateInfoCollection) {
        if (executorService != null) {
            return compensateForceConcurrently(compensateInfoCollection);
        } else {
            return compensateInfoCollection.stream()
                    .map(c -> compensate(c, true))
                    .collect(Collectors.toList());
        }
    }

    private List<CompensateResult> compensateForceConcurrently(Collection<CompensateInfoExt> compensateInfoCollection) {
        List<Future<CompensateResult>> futureList = compensateInfoCollection.stream()
                .map(c -> executorService.submit(() -> compensate(c, true)))
                .collect(Collectors.toList());
        return futureList.stream().map(f -> {
            try {
                return f.get(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new CompensateException(e);
            }
        }).collect(Collectors.toList());
    }


    public CompensateResult compensate(CompensateInfoExt compensateInfo, Boolean force) {
        log.info("发起补偿: {} ", compensateInfo, force);
        // 补偿状态
        CompensateResult result = null;
        if (force) {
            CompensateInfoExt.CompensateStatus compensateStatus = RuleParser.parserToStatus(compensateInfo);
            switch (compensateStatus) {
                case RETRY_ABLE:
                    result = execCompensate(compensateInfo, force);
                    break;
                case ALARM_ABLE:
                    result = execAlarm(compensateInfo);
                    break;
                case OVER_ALARM_COUNT:
                    result = execOverAlarm(compensateInfo);
                    break;
                default:
                    return null;
            }
        }
        result = execCompensate(compensateInfo, force);
        log.info("补偿id: {}, 结果: {}", compensateInfo.getId(), result.getResult());
        return result;
    }


    private CompensateResult execOverAlarm(CompensateInfoExt compensateInfo) {
        compensateStore.deleteCompensateInfo(compensateInfo.getId(), false);
        return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.OVER_ALARM_COUNT);
    }

    /**
     * 报警
     *
     * @param compensateInfo
     * @return
     */
    private CompensateResult execAlarm(CompensateInfoExt compensateInfo) {
        compensateAlarmService.sendAlarm(compensateInfo);
        compensateStore.updateCompensateInfo(compensateInfo.getId(),
                compensateInfo.addAlarmToCompensateInfo(RuleParser.nextTriggerTime(compensateInfo)));
        return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.ALARM);
    }

    /**
     * 补偿重试
     *
     * @param compensateInfo
     * @return
     */
    private CompensateResult execCompensate(CompensateInfoExt compensateInfo, Boolean force) {
        try {
            doCompensate(compensateInfo);
        } catch (Exception e) {
            log.warn("补偿: {} 时发生异常: ", compensateInfo, e);
            if (force) {
                compensateStore.updateCompensateInfo(compensateInfo.getId(), compensateInfo.addRetryToCompensateInfo(null));
                return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.RETRY_FAIL);
            }
            compensateStore.updateCompensateInfo(compensateInfo.getId(),
                    compensateInfo.addRetryToCompensateInfo(RuleParser.nextTriggerTime(compensateInfo)));
            return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.RETRY_FAIL);
        }
        compensateStore.deleteCompensateInfo(compensateInfo.getId(), true);
        return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.RETRY_SUCCESS);
    }

    private void doCompensate(CompensateInfoExt compensateInfo) throws InvocationTargetException {
        Object[] objects = composite.getArgumentResolver(compensateInfo.getCompensateType()).deResolver(compensateInfo);
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

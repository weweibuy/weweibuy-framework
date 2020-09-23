package com.weweibuy.framework.compensate.core;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.compensate.exception.CompensateException;
import com.weweibuy.framework.compensate.model.CompensateInfoExt;
import com.weweibuy.framework.compensate.model.CompensateResult;
import com.weweibuy.framework.compensate.model.CompensateResultEum;
import com.weweibuy.framework.compensate.model.CompensateStatus;
import com.weweibuy.framework.compensate.support.CompensateContext;
import com.weweibuy.framework.compensate.support.CompensateRecorder;
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

    private CompensateRecorder compensateRecorder;


    public CompensateHandlerService(CompensateMethodRegister compensateMethodRegister,
                                    CompensateStore compensateStore, CompensateTypeResolverComposite composite,
                                    CompensateAlarmService compensateAlarmService, ExecutorService executorService, CompensateRecorder compensateRecorder) {
        this.compensateMethodRegister = compensateMethodRegister;
        this.compensateStore = compensateStore;
        this.composite = composite;
        this.compensateAlarmService = compensateAlarmService;
        this.executorService = executorService;
        this.compensateRecorder = compensateRecorder;
    }

    public CompensateHandlerService(ExecutorService executorService, CompensateMethodRegister compensateMethodRegister,
                                    CompensateStore compensateStore, CompensateTypeResolverComposite composite, CompensateRecorder compensateRecorder) {
        this.executorService = executorService;
        this.compensateMethodRegister = compensateMethodRegister;
        this.compensateStore = compensateStore;
        this.composite = composite;
        this.compensateRecorder = compensateRecorder;
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
                    .map(c -> bindContextAndCompensate(c, false))
                    .collect(Collectors.toList());
        }
    }


    private List<CompensateResult> compensateConcurrently(Collection<CompensateInfoExt> compensateInfoCollection) {
        List<Future<CompensateResult>> futureList = compensateInfoCollection.stream()
                .map(c -> executorService.submit(() -> bindContextAndCompensate(c, false)))
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
                    .map(c -> bindContextAndCompensate(c, true))
                    .collect(Collectors.toList());
        }
    }

    private List<CompensateResult> compensateForceConcurrently(Collection<CompensateInfoExt> compensateInfoCollection) {
        List<Future<CompensateResult>> futureList = compensateInfoCollection.stream()
                .map(c -> executorService.submit(() -> bindContextAndCompensate(c, true)))
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
     * 防止 在传播特性中  不应该补偿的方法出现的补偿
     *
     * @param compensateInfo
     * @param force
     * @return
     */
    public CompensateResult bindContextAndCompensate(CompensateInfoExt compensateInfo, Boolean force) {
        CompensateContext.setCompensate();
        try {
            return compensate(compensateInfo, force);
        } finally {
            CompensateContext.removeCompensate();
        }
    }

    public CompensateResult compensate(CompensateInfoExt compensateInfo, Boolean force) {
        log.info("发起补偿: 补偿id: {}, 补偿Key: {}, 业务Id: {}, 补偿方式: {}",
                compensateInfo.getId(),
                compensateInfo.getCompensateKey(),
                compensateInfo.getBizId(),
                compensateInfo.getCompensateType());
        // 补偿状态
        CompensateResult result = null;
        CompensateStatus compensateStatus = null;
        if (!force) {
            compensateStatus = RuleParser.parserToStatus(compensateInfo);
            switch (compensateStatus) {
                case RETRY_ABLE:
                    result = execCompensate(compensateInfo, false);
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
        } else {
            compensateStatus = CompensateStatus.RETRY_ABLE;
            result = execCompensate(compensateInfo, true);
        }
        compensateRecorder.recorderCompensate(result, force, compensateStatus);
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
        Object[] objects = composite.getArgumentResolver(compensateInfo.getCompensateType()).deResolver(compensateInfo);
        CompensateHandlerMethod compensateHandlerMethod = compensateMethodRegister.getCompensateHandlerMethod(compensateInfo.getCompensateKey());
        try {
            doCompensate(compensateHandlerMethod, objects);
        } catch (Exception e) {
            return handlerCompensateException(compensateHandlerMethod, objects, compensateInfo, force, e);
        }
        compensateStore.deleteCompensateInfo(compensateInfo.getId(), true);
        return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.RETRY_SUCCESS);
    }


    private void doCompensate(CompensateHandlerMethod compensateHandlerMethod, Object[] args) throws InvocationTargetException {
        compensateHandlerMethod.invoke(args);
    }


    /**
     * 处理补偿异常
     *
     * @param compensateHandlerMethod
     * @param objects
     * @param compensateInfo
     * @param force
     * @param e
     * @return
     */
    private CompensateResult handlerCompensateException(CompensateHandlerMethod compensateHandlerMethod, Object[] objects,
                                                        CompensateInfoExt compensateInfo, Boolean force, Exception e) {
        Throwable throwable = e instanceof InvocationTargetException ? ((InvocationTargetException) e).getTargetException() : e;
        log.warn("补偿: {} 时发生异常: ", compensateInfo, throwable);
        if (force) {
            // 强制触发 不计入重试次数
            return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.RETRY_FAIL, throwable);
        }
        compensateStore.updateCompensateInfo(compensateInfo.getId(),
                compensateInfo.addRetryToCompensateInfo(RuleParser.nextTriggerTime(compensateInfo)));
        if (compensateHandlerMethod.hasRecoverMethod() &&
                CompensateStatus.ALARM_ABLE.equals(RuleParser.parserToStatus(compensateInfo))) {
            // 执行recover 方法
            try {
                invokeRecover(compensateHandlerMethod, objects);
            } catch (Exception e1) {
                Throwable recoverThrowable = e1 instanceof InvocationTargetException ? ((InvocationTargetException) e1).getTargetException() : e1;
                log.warn("补偿: {}, 调用恢复方法异常:", compensateInfo, recoverThrowable);
                return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.RETRY_FAIL_RECOVER_FAIL, throwable);
            }
            compensateStore.deleteCompensateInfo(compensateInfo.getId(), false);
            return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.RETRY_FAIL_RECOVER_SUCCESS, throwable);
        }
        return CompensateResult.fromCompensateInfoExt(compensateInfo, CompensateResultEum.RETRY_FAIL, throwable);
    }


    private void invokeRecover(CompensateHandlerMethod compensateHandlerMethod, Object[] args) {
        if (executorService != null) {
            executorService.execute(() ->
                    doInvokeRecover(compensateHandlerMethod, args));
        } else {
            doInvokeRecover(compensateHandlerMethod, args);
        }
    }

    private void doInvokeRecover(CompensateHandlerMethod compensateHandlerMethod, Object[] objects) {
        try {
            compensateHandlerMethod.invokeRecover(objects);
        } catch (InvocationTargetException e) {
            throw Exceptions.system("执行补偿Recover方法异常", e.getCause());
        }
    }

}

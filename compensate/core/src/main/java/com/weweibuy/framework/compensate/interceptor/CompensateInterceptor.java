package com.weweibuy.framework.compensate.interceptor;


import com.weweibuy.framework.common.core.utils.OptionalEnhance;
import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.annotation.Propagation;
import com.weweibuy.framework.compensate.core.CompensateAlarmService;
import com.weweibuy.framework.compensate.core.CompensateStore;
import com.weweibuy.framework.compensate.model.CompensateInfo;
import com.weweibuy.framework.compensate.support.CompensateAnnotationMetaDataParser;
import com.weweibuy.framework.compensate.support.CompensateContext;
import com.weweibuy.framework.compensate.support.CompensateRecorder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;

/**
 * 补偿切面
 *
 * @author durenhao
 * @date 2020/2/13 21:06
 **/
public class CompensateInterceptor implements MethodInterceptor {

    private final CompensateStore compensateStore;

    private final CompensateAnnotationMetaDataParser metaDataParser;

    private ExecutorService executorService;

    private CompensateAlarmService compensateAlarmService;

    private CompensateRecorder compensateRecorder;


    public CompensateInterceptor(CompensateStore compensateStore,
                                 CompensateAnnotationMetaDataParser metaDataParser, CompensateAlarmService compensateAlarmService, CompensateRecorder compensateRecorder) {
        this.compensateStore = compensateStore;
        this.metaDataParser = metaDataParser;
        this.compensateAlarmService = compensateAlarmService;
        this.compensateRecorder = compensateRecorder;
    }

    public CompensateInterceptor(CompensateStore compensateStore, CompensateAnnotationMetaDataParser metaDataParser,
                                 ExecutorService executorService, CompensateAlarmService compensateAlarmService, CompensateRecorder compensateRecorder) {
        this.compensateStore = compensateStore;
        this.metaDataParser = metaDataParser;
        this.executorService = executorService;
        this.compensateAlarmService = compensateAlarmService;
        this.compensateRecorder = compensateRecorder;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        boolean bind = false;
        Boolean compensate = CompensateContext.getCompensate();
        if (compensate == null) {
            CompensateContext.setCompensate();
            bind = true;
        }
        try {
            return methodInvocation.proceed();
        } catch (Exception e) {
            Compensate annotation = methodInvocation.getMethod().getAnnotation(Compensate.class);
            Boolean bindCompensate = CompensateContext.getCompensate();
            if (bind || bindCompensate == null || annotation.propagation().equals(Propagation.REQUIRES_NEW)) {
                OptionalEnhance.ofNullable(executorService)
                        .ifPresentOrElse(es -> es.execute(() -> parseAndSaveCompensateInfo(methodInvocation, e)),
                                () -> parseAndSaveCompensateInfo(methodInvocation, e));
            }
            throw e;
        } finally {
            if (bind) {
                CompensateContext.removeCompensate();
            }
        }
    }

    private void parseAndSaveCompensateInfo(MethodInvocation methodInvocation, Exception e) {
        Compensate annotation = methodInvocation.getMethod().getAnnotation(Compensate.class);
        if (metaDataParser.shouldCompensate(annotation, e)) {
            CompensateInfo compensateInfo = metaDataParser.toCompensateInfo(annotation, methodInvocation.getThis(),
                    methodInvocation.getMethod(), methodInvocation.getArguments());
            String id = null;
            try {
                id = compensateStore.saveCompensateInfo(compensateInfo);
            } catch (Exception e1) {
                compensateAlarmService.sendSaveCompensateAlarm(compensateInfo, e1);
            } finally {
                if (StringUtils.isNotBlank(id)) {
                    compensateRecorder.recorderCompensateCreate(id, e);
                }
            }
        }
    }
}

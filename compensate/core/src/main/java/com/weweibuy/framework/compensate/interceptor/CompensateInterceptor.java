package com.weweibuy.framework.compensate.interceptor;


import com.weweibuy.framework.compensate.interfaces.CompensateStore;
import com.weweibuy.framework.compensate.interfaces.annotation.Compensate;
import com.weweibuy.framework.compensate.interfaces.model.CompensateInfo;
import com.weweibuy.framework.compensate.support.CompensateAnnotationMetaDataParser;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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

    public CompensateInterceptor(CompensateStore compensateStore,
                                 CompensateAnnotationMetaDataParser metaDataParser) {
        this.compensateStore = compensateStore;
        this.metaDataParser = metaDataParser;
    }

    public CompensateInterceptor(CompensateStore compensateStore, CompensateAnnotationMetaDataParser metaDataParser,
                                 ExecutorService executorService) {
        this.compensateStore = compensateStore;
        this.metaDataParser = metaDataParser;
        this.executorService = executorService;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        } catch (Exception e) {
            if (executorService != null) {
                executorService.execute(() -> parseAndSaveCompensateInfo(methodInvocation, e));
            } else {
                parseAndSaveCompensateInfo(methodInvocation, e);
            }
            throw e;
        }
    }

    private void parseAndSaveCompensateInfo(MethodInvocation methodInvocation, Exception e) {
        Compensate annotation = methodInvocation.getMethod().getAnnotation(Compensate.class);
        if (metaDataParser.shouldCompensate(annotation, e)) {
            CompensateInfo compensateInfo = metaDataParser.toCompensateInfo(annotation, methodInvocation.getThis(),
                    methodInvocation.getMethod(), methodInvocation.getArguments());
            compensateStore.saveCompensateInfo(compensateInfo);
        }
    }
}

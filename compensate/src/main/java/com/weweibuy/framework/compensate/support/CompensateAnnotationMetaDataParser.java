package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.CompensateConfigProperties;
import com.weweibuy.framework.compensate.core.CompensateConfigStore;
import com.weweibuy.framework.compensate.core.CompensateInfo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解数据解析器
 *
 * @author durenhao
 * @date 2020/2/14 16:56
 **/
public class CompensateAnnotationMetaDataParser {

    private CompensateConfigStore compensateConfigStore;

    private Map<String, BinaryExceptionClassifier> shouldCompensateMap = new ConcurrentHashMap<>();


    public CompensateInfo parseCompensate(Compensate annotation, Object target, Method method, Object[] args) {
        CompensateInfo compensateInfo = new CompensateInfo();
        String key = annotation.key();
        compensateInfo.setCompensateKey(key);
        CompensateConfigProperties configProperties = compensateConfigStore.compensateConfig(key);
        if (configProperties == null) {
            throw new IllegalStateException("补偿Key " + key + ",必须有对应的配置");
        }
        Integer compensateType = configProperties.getCompensateType();

        return compensateInfo;
    }


    public boolean shouldCompensate(Compensate annotation, Exception e) {
        String key = annotation.key();

        BinaryExceptionClassifier classifier = shouldCompensateMap.computeIfAbsent(key, k -> {
            Map<Class<? extends Throwable>, Boolean> compensateMap = new HashMap<>(8);
            Class<? extends Throwable>[] include = annotation.include();
            Class<? extends Throwable>[] exclude = annotation.exclude();
            for (Class<? extends Throwable> i : include) {
                compensateMap.put(i, true);
            }
            for (Class<? extends Throwable> i : exclude) {
                compensateMap.put(i, false);
            }
            return new BinaryExceptionClassifier(compensateMap, false, true);
        });

        return classifier.classify(e);
    }

}

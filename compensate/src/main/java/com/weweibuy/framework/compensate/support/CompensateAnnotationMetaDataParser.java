package com.weweibuy.framework.compensate.support;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.core.CompensateInfo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author durenhao
 * @date 2020/2/14 16:56
 **/
public class CompensateAnnotationMetaDataParser {

    private Map<String, BinaryExceptionClassifier> shouldCompensateMap = new ConcurrentHashMap<>();


    public CompensateInfo parseCompensate(Compensate annotation, Method method) {
        CompensateInfo compensateInfo = new CompensateInfo();
        compensateInfo.setCompensateKey(annotation.key());
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

package com.weweibuy.framework.idempotent.core.aspect;

import com.weweibuy.framework.idempotent.core.annotation.Idempotent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 注解上元数据持有者
 *
 * @author durenhao
 * @date 2020/6/19 23:41
 **/
public class AnnotationMetaDataHolder {

    private Map<Method, IdempotentAnnotationMeta> hashMap = new HashMap();

    public void putMetaData(Method method, Idempotent idempotent) {
        hashMap.put(method, new IdempotentAnnotationMeta(idempotent));
    }

    public IdempotentAnnotationMeta getMetaData(Method method) {
        return hashMap.get(method);
    }

}

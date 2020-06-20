package com.weweibuy.framework.idempotent.core.support;

import com.fasterxml.jackson.databind.JavaType;
import com.weweibuy.framework.idempotent.core.annotation.Idempotent;
import lombok.Getter;

/**
 * @author durenhao
 * @date 2020/6/19 23:41
 **/
@Getter
public class IdempotentAnnotationMeta {

    private final String key;

    private final String sharding;

    private final Long maxLockMilli;

    private final String generator;

    private final JavaType returnType;

    private final String idempotentManager;

    public IdempotentAnnotationMeta(Idempotent idempotent, JavaType javaType) {
        key = idempotent.key();
        sharding = idempotent.sharding();
        maxLockMilli = idempotent.maxLockMilli();
        generator = idempotent.generator();
        idempotentManager = idempotent.idempotentManager();
        returnType = javaType;
    }


}

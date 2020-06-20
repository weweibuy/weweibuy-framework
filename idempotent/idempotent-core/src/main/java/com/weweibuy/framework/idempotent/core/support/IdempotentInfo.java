package com.weweibuy.framework.idempotent.core.support;

import com.fasterxml.jackson.databind.JavaType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author durenhao
 * @date 2020/6/19 23:33
 **/
@Getter
@AllArgsConstructor
public class IdempotentInfo {

    private final String key;

    private final String sharding;

    private final Long maxLockMilli;

    private final JavaType javaType;

    private final IdempotentManager idempotentManager;

}

package com.weweibuy.framework.idempotent.core.aspect;

import com.weweibuy.framework.idempotent.core.annotation.Idempotent;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * 幂等切点
 *
 * @author durenhao
 * @date 2020/3/31 14:54
 **/
public class IdempotentPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return method.getAnnotation(Idempotent.class) != null;
    }
}

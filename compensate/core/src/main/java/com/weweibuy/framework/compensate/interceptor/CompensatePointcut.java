package com.weweibuy.framework.compensate.interceptor;

import com.weweibuy.framework.compensate.annotation.Compensate;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/2/13 21:19
 **/
public class CompensatePointcut extends StaticMethodMatcherPointcut {


    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return method.getAnnotation(Compensate.class) != null;
    }
}

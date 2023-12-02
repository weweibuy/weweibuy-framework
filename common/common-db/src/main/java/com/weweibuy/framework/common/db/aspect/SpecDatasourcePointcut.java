package com.weweibuy.framework.common.db.aspect;

import com.weweibuy.framework.common.db.annotation.SpecDatasource;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

/**
 * 幂等切点
 *
 * @author durenhao
 * @date 2023/12/02 14:53
 **/
public class SpecDatasourcePointcut extends StaticMethodMatcherPointcut {


    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return method.getAnnotation(SpecDatasource.class) != null;
    }
}

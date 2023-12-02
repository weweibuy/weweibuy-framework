package com.weweibuy.framework.common.db.aspect;

import com.weweibuy.framework.common.db.annotation.SpecDataSource;
import com.weweibuy.framework.common.db.multiple.SpecDataSourceContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 指定数据源name切面
 *
 * @author durenhao
 * @date 2023/12/02 14:53
 **/
public class SpecDataSourceAspect implements MethodInterceptor {


    /**
     * 通知
     *
     * @param methodInvocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        SpecDataSource annotation = methodInvocation.getMethod().getAnnotation(SpecDataSource.class);
        String datasourceName = annotation.value();
        SpecDataSourceContext.setSpecDatasource(datasourceName);
        try {
            return methodInvocation.proceed();
        } finally {
            SpecDataSourceContext.clear();
        }
    }
}

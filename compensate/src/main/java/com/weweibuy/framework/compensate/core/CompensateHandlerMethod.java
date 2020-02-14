package com.weweibuy.framework.compensate.core;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2020/2/14 19:56
 **/
@Data
@Builder
public class CompensateHandlerMethod {

    private Object bean;

    private Method method;

    private Object recoverBean;

    private Method recoverMethod;

}

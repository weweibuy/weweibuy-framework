package com.weweibuy.framework.biztask.core;

import com.weweibuy.framework.biztask.db.po.BizTask;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author durenhao
 * @date 2024/1/20 13:35
 **/
@Data
class BizTaskHandlerMethod {

    private Object bean;

    private Method bridgedMethod;

    private String taskType;

    private Integer bizStatus;

    void invokeMethod(BizTask bizTask) throws Exception {
        bridgedMethod.invoke(bean, bizTask);
    }
}

package com.weweibuy.framework.biztask.core;

import com.weweibuy.framework.biztask.annotation.ExecBizTask;
import com.weweibuy.framework.biztask.db.po.BizTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.BridgeMethodResolver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author durenhao
 * @date 2024/1/20 13:44
 **/
@Slf4j
public class BizTaskHandlerMethodHolder {

    private Map<String, BizTaskHandlerMethod> handlerMethodMap;

    void buildHandlerMethod(Object bean, Method method, ExecBizTask bizTask) {
        if (handlerMethodMap == null) {
            handlerMethodMap = new HashMap<>();
        }
        String type = bizTask.type();
        int bizStatus = bizTask.bizStatus();
        BizTaskHandlerMethod bizTaskHandlerMethod = new BizTaskHandlerMethod();
        bizTaskHandlerMethod.setTaskType(type);
        bizTaskHandlerMethod.setBizStatus(bizStatus);
        bizTaskHandlerMethod.setBean(bean);
        bizTaskHandlerMethod.setBridgedMethod(BridgeMethodResolver.findBridgedMethod(method));

        handlerMethodMap.put(mapKey(type, bizStatus), bizTaskHandlerMethod);
    }

    private String mapKey(String type, int bizStatus) {
        return type + "_" + bizStatus;
    }


    BizTaskHandlerMethod findHandlerMethod(BizTask bizTask) {
        String bizType = bizTask.getBizType();
        Integer bizStatus = bizTask.getBizStatus();
        return handlerMethodMap.get(mapKey(bizType, bizStatus));
    }
}

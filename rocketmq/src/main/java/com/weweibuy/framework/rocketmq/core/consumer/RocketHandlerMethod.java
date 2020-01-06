package com.weweibuy.framework.rocketmq.core.consumer;

import org.apache.rocketmq.common.message.MessageExt;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author durenhao
 * @date 2020/1/6 23:14
 **/
public class RocketHandlerMethod {

    private HandlerMethodArgumentResolverComposite argumentResolverComposite;

    private Object bean;

    private Method method;


    public Object invoke(List<MessageExt> messageExtList, Object... providedArgs) throws Exception {
        Object[] args = getMethodArgumentValues(messageExtList, providedArgs);
        Object returnValue = doInvoke(args);
        return returnValue;
    }

    /**
     * 解析方法参数
     *
     * @param messageExtList
     * @param providedArgs
     * @return
     */
    private Object[] getMethodArgumentValues(List<MessageExt> messageExtList, Object[] providedArgs) {
        return null;
    }

    /**
     * 调用模板方法
     *
     * @param args
     * @return
     */
    private Object doInvoke(Object[] args) {
        return null;
    }


}

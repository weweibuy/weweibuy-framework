package com.weweibuy.framework.rocketmq.core.consumer;

/**
 * MQ 消费异常 异常处理
 *
 * @author durenhao
 * @date 2020/1/12 22:34
 **/
public interface RocketListenerErrorHandler {

    /**
     * 处理异常
     *
     * @param e             异常
     * @param messageObject 消息体 List<MessageExt>  or Message </>
     * @param orderly       是否顺序消费
     * @return true: 消费成功 false:消费失败  如果是在 批量一起消费是, 只要有一个false  一批消息都将消费失败
     */
    boolean handlerException(Exception e, Object messageObject, Boolean orderly);


}

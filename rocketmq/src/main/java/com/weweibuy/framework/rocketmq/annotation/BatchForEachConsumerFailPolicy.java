package com.weweibuy.framework.rocketmq.annotation;

/**
 * 批量迭代消费,失败策略
 *
 * @author durenhao
 * @date 2020/6/26 16:40
 **/
public enum BatchForEachConsumerFailPolicy {

    /**
     * 匹配第一条失败, 如何出现一条失败,后续不在继续迭代消费, 直接回复消息失败
     */
    MATCH_FIRST_FAIL,

    /**
     * 继续迭代其他消息,迭代完成之后,回复消息失败
     */
    CONTINUE_OTHERS,
    ;

}

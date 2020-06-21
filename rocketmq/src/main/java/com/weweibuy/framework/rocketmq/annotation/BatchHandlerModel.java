package com.weweibuy.framework.rocketmq.annotation;


/**
 * 批量消费模式
 *
 * @author durenhao
 * @date 2020/1/18 18:14
 **/
public enum BatchHandlerModel {

    /**
     * 迭代消费处理
     */
    FOREACH,

    /**
     * 一起消费处理
     */
    TOGETHER,
    ;


}

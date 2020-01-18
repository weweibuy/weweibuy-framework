package com.weweibuy.framework.rocketmq.annotation;


/**
 * 批量处理模式
 *
 * @author durenhao
 * @date 2020/1/18 18:14
 **/
public enum BatchHandlerModel {

    /**
     * 迭代处理
     */
    FOREACH,

    /**
     * 一起处理
     */
    TOGETHER,;


}

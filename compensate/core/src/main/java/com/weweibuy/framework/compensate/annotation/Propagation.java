package com.weweibuy.framework.compensate.annotation;

/**
 * 补偿的传播特性
 *
 * @author durenhao
 * @date 2020/6/25 17:00
 **/
public enum Propagation {

    /**
     * 没有就创建 有就加入
     */
    REQUIRED,

    /**
     * 有没有都创建
     */
    REQUIRES_NEW,
    ;


}

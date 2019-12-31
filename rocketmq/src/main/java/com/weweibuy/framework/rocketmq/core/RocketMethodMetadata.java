package com.weweibuy.framework.rocketmq.core;

import lombok.Data;

/**
 * 方法元数据
 *
 * @author durenhao
 * @date 2019/12/30 20:51
 **/
@Data
public class RocketMethodMetadata {

    private String topic;

    private int bodyIndex;

    private int tagIndex;

    private int keyIndex;

    private boolean orderly;

    private boolean oneWay;

    private int timeout;

    private String tag;

    private String keyExpression;
}

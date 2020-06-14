package com.weweibuy.framework.common.feign.support;

import feign.Retryer;

/**
 * @author durenhao
 * @date 2020/6/14 9:39
 **/
public class ThreeTimeRetryer extends Retryer.Default {

    public ThreeTimeRetryer() {
        super(0, 0, 3);
    }

}

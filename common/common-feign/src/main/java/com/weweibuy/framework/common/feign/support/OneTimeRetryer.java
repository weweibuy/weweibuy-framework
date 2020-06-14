package com.weweibuy.framework.common.feign.support;

import feign.Retryer;

/**
 * @author durenhao
 * @date 2020/6/14 9:39
 **/
public class OneTimeRetryer extends Retryer.Default {

    public OneTimeRetryer() {
        super(0, 0, 1);
    }

}

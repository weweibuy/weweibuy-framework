package com.weweibuy.framework.common.feign.support;

import feign.Retryer;

/**
 * @author durenhao
 * @date 2020/6/14 9:39
 **/
public class TwoTimeRetryer extends Retryer.Default {

    public TwoTimeRetryer() {
        super(0, 0, 2);
    }

}

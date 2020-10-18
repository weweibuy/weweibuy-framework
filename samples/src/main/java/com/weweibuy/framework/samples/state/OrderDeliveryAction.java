package com.weweibuy.framework.samples.state;

import org.springframework.stereotype.Component;

/**
 * @author : Knight
 * @date : 2020/10/18 4:02 下午
 */
@Component
public class OrderDeliveryAction extends StateAction {

    @Override
    protected Object doAction(Object data, String event, String source, String target) {
        return null;
    }
}

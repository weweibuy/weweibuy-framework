package com.weweibuy.framework.samples.state;

import com.weweibuy.framerwork.statemachine.provider.AbstractStateAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author : Knight
 * @date : 2020/10/18 4:02 下午
 */
@Component
@RequiredArgsConstructor
public class OrderDeliveryAction extends AbstractStateAction {
    private final OrderService orderService;

    @Override
    protected Object doAction (Object data, String event, String source, String target) {
        //校验状态
        return orderService.deliveryOrder((String) data, target);
    }
}

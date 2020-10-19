package com.weweibuy.framework.samples.state;

import com.weweibuy.framerwork.statemachine.provider.StateAction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author : Knight
 * @date : 2020/10/18 3:54 下午
 */
@Component
@RequiredArgsConstructor
public class OrderCreateAction extends StateAction {
    private final OrderService orderService;

    // 可在此处扩张自己的业务逻辑 在业务需要校验 持久层数据状态是否与入口一致
    @Override
    protected Object doAction(Object data, String event, String source, String target) {
        return orderService.createNumber((String) data, target);
    }


}

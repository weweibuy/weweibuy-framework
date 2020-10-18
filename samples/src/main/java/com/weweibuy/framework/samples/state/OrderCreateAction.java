package com.weweibuy.framework.samples.state;

import com.weweibuy.framerwork.statemachine.provider.StateAction;
import org.springframework.stereotype.Component;

/**
 * @author : Knight
 * @date : 2020/10/18 3:54 下午
 */
@Component
public class OrderCreateAction extends StateAction {

    @Override
    protected Object doAction(Object data, String event, String source, String target) {
        return null;
    }


}

package com.weweibuy.framerwork.statemachine.core;

import com.weweibuy.framerwork.statemachine.support.ResultHolder;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;

/**
 * @author : Knight
 * @date : 2020/10/18 3:12 下午
 */
public class StateMachineService {
    private final PersistStateMachineHandler handler;

    public static String DATA = "data";

    public static final String RESULT = "result";

    public StateMachineService(PersistStateMachineHandler handler) {
        this.handler = handler;
    }

    public Object change(String event, Object obj, String currentState) {
        ResultHolder resultHolder = new ResultHolder();
        handler.handleEventWithState(MessageBuilder.withPayload(event).setHeader(DATA, obj).setHeader(RESULT, resultHolder).build(), currentState);
        return resultHolder.getResultData();
    }
}


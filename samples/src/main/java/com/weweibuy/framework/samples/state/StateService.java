package com.weweibuy.framework.samples.state;

import com.weweibuy.framerwork.statemachine.common.enums.TurnstileEvents;
import com.weweibuy.framerwork.statemachine.common.enums.TurnstileStates;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : Knight
 * @date : 2020/10/17 5:00 下午
 */
@Service
public class StateService {

    @Resource
    private StateMachine<TurnstileStates, TurnstileEvents> stateMachine;

    public void listState(){
        stateMachine.start();
        System.out.println("--- coin ---");
        stateMachine.sendEvent(TurnstileEvents.COIN);
        System.out.println("--- coin ---");
        stateMachine.sendEvent(TurnstileEvents.COIN);
        System.out.println("--- push ---");
        stateMachine.sendEvent(TurnstileEvents.PUSH);
        System.out.println("--- push ---");
        stateMachine.sendEvent(TurnstileEvents.PUSH);
        stateMachine.stop();
    }
}

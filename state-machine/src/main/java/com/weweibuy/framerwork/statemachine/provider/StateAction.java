package com.weweibuy.framerwork.statemachine.provider;

import org.springframework.statemachine.action.Action;

/**
 * @author : Knight
 * @date : 2020/10/17 4:09 下午
 */
public abstract class StateAction implements Action<String, String> {
//    @Override
//    public void execute(StateContext<String, String> stateContext) {
//        ResultHolder resultHolder = (ResultHolder) stateContext.getMessageHeader(StateMachineService.RESULT);
//        resultHolder.setResultData(doAction(stateContext.getMessageHeader(StateMachineService.DATA), stateContext.getEvent(),
//                stateContext.getSource().getId(), stateContext.getTarget().getId()));
//    }

    protected abstract Object doAction(Object data, String event, String source, String target);
}

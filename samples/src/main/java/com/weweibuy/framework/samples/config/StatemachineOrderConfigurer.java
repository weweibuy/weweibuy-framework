package com.weweibuy.framework.samples.config;

import com.weweibuy.framerwork.statemachine.common.enums.OrderEvent;
import com.weweibuy.framerwork.statemachine.common.enums.OrderState;
import com.weweibuy.framework.samples.state.OrderCreateAction;
import com.weweibuy.framework.samples.state.OrderDeliveryAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * @author : Knight
 * @date : 2020/10/18 3:51 下午
 */
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class StatemachineOrderConfigurer extends StateMachineConfigurerAdapter<String, String> {

    //状态机---action
    @Autowired
    private OrderCreateAction orderCreateAction;
    @Autowired
    private OrderDeliveryAction orderDeliveryAction;

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial(OrderState.INITIALED.name())
                .states(EnumSet.allOf(OrderState.class)
                        .stream().map(Enum::name)
                        .collect(Collectors.toSet()));
    }

    /**
     * 不一定每个event伴随action 如不需要也可以不配置
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderState.INITIALED.name()).target(OrderState.CREATED.name())
                .event(OrderEvent.CREATED.name()).action(orderDeliveryAction);
    }

}

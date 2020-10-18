package com.weweibuy.framerwork.statemachine.config;

import com.weweibuy.framerwork.statemachine.core.StateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;

/**
 * @author : Knight
 * @date : 2020/10/18 4:57 下午
 */
@Configuration
public class StateConfig {

    @Autowired(required = false)
    private StateMachine stateMachine;

    @Bean
    public StateMachineService stateMachineService() {
        return new StateMachineService(persistStateMachineHandler());
    }

    @Bean
    public PersistStateMachineHandler persistStateMachineHandler() {
        return new PersistStateMachineHandler(stateMachine);
    }
}

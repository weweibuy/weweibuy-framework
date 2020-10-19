package com.weweibuy.framework.samples.controller;

import com.weweibuy.framerwork.statemachine.core.StateMachineService;
import com.weweibuy.framework.samples.state.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Knight
 * @date : 2020/10/17 4:57 下午
 */
@RestController
@RequestMapping("/state")
@RequiredArgsConstructor
public class StateController {

    private final StateService stateService;
    private final StateMachineService stateMachineService;

    @GetMapping("/list")
    public ResponseEntity<String> state() {
        stateService.listState();
        return ResponseEntity.ok("teat ok");
    }

    @GetMapping("/machine/order")
    public ResponseEntity<Object> orderMachine(@RequestParam String event, @RequestParam String state) {
        return ResponseEntity.ok(stateMachineService.change(event, null, state));
    }
}

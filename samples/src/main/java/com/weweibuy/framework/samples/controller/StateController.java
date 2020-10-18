package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : Knight
 * @date : 2020/10/17 4:57 下午
 */
@RestController
@RequestMapping("/state")
public class StateController {

    @Autowired
    private StateService stateService;

    @GetMapping("/list")
    public ResponseEntity state(){
        stateService.listState();
        return ResponseEntity.ok("teat ok");
    }
}

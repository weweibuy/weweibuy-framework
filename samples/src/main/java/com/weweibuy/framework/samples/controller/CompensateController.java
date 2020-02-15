package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.compensate.CompensateSimpleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2020/2/14 14:29
 **/
@RestController
public class CompensateController {

    private final CompensateSimpleService simpleService;

    public CompensateController(CompensateSimpleService simpleService) {
        this.simpleService = simpleService;
    }

    @GetMapping("/compensate")
    public String run(String args) {
        return simpleService.run();
    }

}

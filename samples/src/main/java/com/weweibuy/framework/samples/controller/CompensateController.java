package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.compensate.service.CompensateSimpleService;
import com.weweibuy.framework.samples.model.Dog;
import com.weweibuy.framework.samples.model.User;
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
        User user = new User();
        Dog dog = new Dog();
        user.setName("Jack");
        user.setAge(12);
        dog.setName("tom");
        dog.setAge(12);
        user.setDog(dog);
        simpleService.run();
        return "success";
    }

    @GetMapping("/compensate2")
    public String run2(String args) {
        User user = new User();
        Dog dog = new Dog();
        user.setName("Jack");
        user.setAge(12);
        dog.setName("tom");
        dog.setAge(12);
        user.setDog(dog);
        simpleService.run3(user, dog);
        return "success";
    }

}

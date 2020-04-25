package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.compensate.service.CompensateSimpleService;
import com.weweibuy.framework.samples.model.Dog;
import com.weweibuy.framework.samples.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;

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

    @GetMapping("/compensate3")
    public String run3(String args) {
        User user = new User();
        Dog dog = new Dog();
        user.setName("Jack");
        user.setAge(12);
        dog.setName("tom");
        dog.setAge(12);
        user.setDog(dog);
        simpleService.run3(Collections.singleton(user));
        return "success";
    }

    @GetMapping("/compensate4")
    public String run4(String args) {
        simpleService.run4(args, "zzz");
        return "success";
    }

    @GetMapping("/compensate5")
    public String run5(String args) {
        User user = new User();
        Dog dog = new Dog();
        user.setName("Jack");
        user.setAge(12);
        dog.setName("tom");
        dog.setAge(12);
        user.setDog(dog);
        simpleService.run5(user, dog);
        return "success";
    }

    @GetMapping("/compensate6")
    public String run6(String args) {
        simpleService.run3(Collections.emptyList());
        return "success";
    }

    @GetMapping("/compensate7")
    public String run7(String args) {
        User user = new User();
        Dog dog = new Dog();
        user.setName("Jack");
        user.setAge(12);
        dog.setName("tom");
        dog.setAge(12);
        user.setDog(dog);
        simpleService.run6(user, dog, dog, dog);
        return "success";
    }

    @GetMapping("/compensate8")
    public String run8(String args) {
        User user = new User();
        Dog dog = new Dog();
        user.setName("Jack");
        user.setAge(12);
        user.setDog(dog);
        simpleService.run7(user, user, user, user, user, user, user, user);
        return "success";
    }

    @GetMapping("/compensate9")
    public String run9(String args) {
        User user = new User();
        Dog dog = new Dog();
        user.setName("Jack");
        user.setAge(12);
        user.setDog(dog);
        HashMap<String, User> hashMap = new HashMap<>();
        hashMap.put("121", user);
        hashMap.put("121", user);
        simpleService.run8(hashMap);
        return "success";
    }


}

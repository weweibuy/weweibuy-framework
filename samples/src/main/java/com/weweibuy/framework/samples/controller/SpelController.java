package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.exception.BusinessException;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.samples.model.Dog;
import com.weweibuy.framework.samples.model.User;
import com.weweibuy.framework.samples.spel.SpelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author durenhao
 * @date 2020/8/2 10:54
 **/
@Slf4j
@RestController
@RequestMapping("/spel")
public class SpelController {


    private final SpelService spelService;

    public SpelController(SpelService spelService) {
        this.spelService = spelService;
    }

    @GetMapping("/spel")
    public Object spel(String spel) {
        User user = new User();
        user.setName("tom");
        user.setAge(12);
        user.setTotal(new BigDecimal("10"));
        user.setPrice(new BigDecimal("10.1"));
        Dog dog = new Dog();
        dog.setName("jack");
        dog.setAge(2);
        user.setDog(dog);
        return spelService.evaluatorExpressionStr(spel, user);
    }

}

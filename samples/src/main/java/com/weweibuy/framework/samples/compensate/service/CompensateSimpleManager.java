package com.weweibuy.framework.samples.compensate.service;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.samples.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author durenhao
 * @date 2020/2/14 14:27
 **/
@Component
@Slf4j
public class CompensateSimpleManager {

    private Integer integer = 0;


    @Compensate(key = "CompensateSimpleManager_8", bizId = "#userMap")
    public String run8(Map<String, User> userMap) {
        integer = 0;
        return "success";
    }
}

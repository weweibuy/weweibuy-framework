package com.weweibuy.framework.samples.compensate.service;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.annotation.Recover;
import com.weweibuy.framework.samples.model.Dog;
import com.weweibuy.framework.samples.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author durenhao
 * @date 2020/2/14 14:27
 **/
@Service("compensateSimpleService")
@Slf4j
public class CompensateSimpleService {

    private Integer integer = 0;

    @Compensate(key = "CompensateSimpleService", recover = @Recover(beanName = "compensateSimpleService", method = "run2"))
    public String run() {
        if (integer < 1) {
            integer++;
            throw new RuntimeException("....");
        }
        return "success";
    }

    @Compensate(key = "CompensateSimpleService_2", bizId = "#user.name + '_' + #dog.age")
    public String run3(User user, Dog dog) {
        if (integer < 2) {
            integer++;
            throw new RuntimeException("....");
        }
        return "success";
    }

    private void run2(String str) {
        log.info("run2 .... {} {} {}", str);
    }
}

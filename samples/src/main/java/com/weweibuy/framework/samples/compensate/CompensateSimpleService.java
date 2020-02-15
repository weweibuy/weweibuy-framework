package com.weweibuy.framework.samples.compensate;

import com.weweibuy.framework.compensate.annotation.Compensate;
import com.weweibuy.framework.compensate.annotation.Recover;
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

    private void run2(String str) {
        log.info("run2 .... {} {} {}",str );
    }
}

package com.weweibuy.framework.samples.compensate;

import com.weweibuy.framework.compensate.annotation.Compensate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author durenhao
 * @date 2020/2/14 14:27
 **/
@Service
@Slf4j
public class CompensateSimpleService {

    private Integer integer = 0;

    @Compensate(key = "CompensateSimpleService")
    public String run() {
        log.info("输入参数为: {}");
        if (integer == 0) {
            integer ++;
            throw new RuntimeException("....");
        }
        return "success";
    }


}

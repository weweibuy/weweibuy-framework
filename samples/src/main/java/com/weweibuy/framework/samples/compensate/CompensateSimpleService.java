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

    @Compensate(key = "CompensateSimpleService")
    public String run(String args) {
        log.info("输入参数为: {}", args);
        if (true) {
            throw new RuntimeException("....");
        }
        return "success";
    }


}

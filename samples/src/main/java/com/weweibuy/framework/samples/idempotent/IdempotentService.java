package com.weweibuy.framework.samples.idempotent;

import com.weweibuy.framework.idempotent.core.annotation.Idempotent;
import com.weweibuy.framework.samples.model.User;
import com.weweibuy.framework.samples.model.dto.CommonCodeJsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author durenhao
 * @date 2020/6/20 16:04
 **/
@Slf4j
@Service
public class IdempotentService {

    @Transactional(rollbackFor = Exception.class)
    @Idempotent(key = "#root.targetClassName + '_' +#root.methodName")
    public void doBiz(){
        log.info("Idempotent biz ......");
    }

    @Transactional(rollbackFor = Exception.class)
    @Idempotent(key = "#user.name + '_' + #user.age")
    public String doBiz2(User user){
        log.info("Idempotent biz ......");
        return "null";
    }

    @Transactional(rollbackFor = Exception.class)
    @Idempotent(key = "", sharding = "#user.name")
    public CommonCodeJsonResponse doBiz3(User user){
        log.info("Idempotent biz ......");
        return CommonCodeJsonResponse.success();
    }

    @Idempotent(key = "", sharding = "#user.name", idempotentManager = "redisIdempotentManager")
    public CommonCodeJsonResponse doBiz4(User user) throws InterruptedException {
        log.info("Idempotent biz ......");
        return CommonCodeJsonResponse.success();
    }


}

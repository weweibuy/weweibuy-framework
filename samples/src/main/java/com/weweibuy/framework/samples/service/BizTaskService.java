package com.weweibuy.framework.samples.service;

import com.weweibuy.framework.biztask.annotation.ExecBizTask;
import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author durenhao
 * @date 2024/1/21 15:54
 **/
@Slf4j
@Service
public class BizTaskService {



    @ExecBizTask(taskType = "test2", bizStatus = 1)
    public void execBizTask3(BizTask bizTask) {
        log.info("任务: {}", JackJsonUtils.writeCamelCase(bizTask));
        throw new RuntimeException("x");
    }

}

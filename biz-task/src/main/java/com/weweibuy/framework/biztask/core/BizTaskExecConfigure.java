package com.weweibuy.framework.biztask.core;

import java.util.Optional;

/**
 * @author durenhao
 * @date 2024/1/21 10:38
 **/
public interface BizTaskExecConfigure {

    BizTaskConfigure DEFAULT = new BizTaskConfigure("30s 1m 2m 5m 10m 20m 30m 1H 2H 4H", 10, 5);

    BizTaskConfigure taskConfigure(String taskType);

    default BizTaskConfigure getOrDefault(String taskType) {
        return Optional.ofNullable(taskConfigure(taskType))
                .orElse(DEFAULT);
    }

}

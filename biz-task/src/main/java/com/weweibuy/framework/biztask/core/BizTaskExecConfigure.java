package com.weweibuy.framework.biztask.core;

import java.util.Optional;

/**
 * @author durenhao
 * @date 2024/1/21 10:38
 **/
public interface BizTaskExecConfigure {

    Integer DEFAULT_MAX_EXEC_COUNT = 10;

    Integer DEFAULT_ALARM_AT_COUNT = 5;

    String DEFAULT_RULE = "30s 1m 2m 5m 10m 20m 30m 1H ...";

    BizTaskConfigure DEFAULT = new BizTaskConfigure(DEFAULT_RULE, DEFAULT_MAX_EXEC_COUNT, DEFAULT_ALARM_AT_COUNT);

    BizTaskConfigure taskConfigure(String taskType);

    default BizTaskConfigure getOrDefault(String taskType) {
        return Optional.ofNullable(taskConfigure(taskType))
                .orElse(DEFAULT);
    }

}

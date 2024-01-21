package com.weweibuy.framework.biztask.core;

import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.biztask.db.repository.BizTaskRepository;
import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author durenhao
 * @date 2024/1/19 21:45
 **/
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractBizTaskTrigger {

    private final BizTaskRepository bizTaskRepository;

    private final BizTaskExecutor bizTaskExecutor;

    private static final Integer DEFAULT_LIMIT = 100;

    public void trigger(Integer taskPartition, Integer totalPartition, Integer limit) {

        if (taskPartition != null && totalPartition != null && taskPartition > totalPartition) {
            throw Exceptions.system(String.format("错误的分区, 当前分区: %s, 总计分区: %s", taskPartition, totalPartition));
        }
        if (taskPartition != null && totalPartition != null && taskPartition == 1 && totalPartition == 1) {
            taskPartition = null;
            totalPartition = null;
        }

        if (limit == null || limit < 1) {
            limit = DEFAULT_LIMIT;
        }

        queryBizTask(taskPartition, totalPartition, limit, 0L);

    }


    private void queryBizTask(Integer taskPartition, Integer totalPartition, Integer limit, Long startId) {
        List<BizTask> bizTaskList = bizTaskRepository.selectExecIngTask(taskPartition, totalPartition, limit, startId);

        if (CollectionUtils.isEmpty(bizTaskList)) {
            return;
        }

        bizTaskExecutor.execTaskList(bizTaskList);

        int size = bizTaskList.size();
        if (size >= limit) {
            startId = bizTaskList.get(size - 1).getId();
            queryBizTask(taskPartition, totalPartition, limit, startId);
        }
    }


}

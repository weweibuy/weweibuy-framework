package com.weweibuy.framework.biztask.core;

import com.weweibuy.framework.biztask.db.po.BizTask;
import com.weweibuy.framework.biztask.support.BizTaskHelper;
import com.weweibuy.framework.common.core.utils.BeanCopyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author durenhao
 * @date 2024/1/20 10:55
 **/
@Slf4j
@RequiredArgsConstructor
public abstract class BizTaskExecutor {

    private final BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder;

    protected abstract void execTaskList(List<BizTask> bizTaskList);

    protected void execTask(BizTask bizTask) {
        BizTask copyBizTask = BeanCopyUtils.copy(bizTask, BizTask.class);

        try {
            log.info("执行业务任务: {}", bizTask);
            execTask0(bizTask);
        } catch (Exception e) {
            log.warn("执行业务任务: {}, 异常: ", bizTask, e);
            handlerBizTaskExecException(copyBizTask, e);
        }
    }


    private void execTask0(BizTask bizTask) throws Exception {
        BizTaskHandlerMethod handlerMethod = bizTaskHandlerMethodHolder.findHandlerMethod(bizTask);
        if (handlerMethod == null) {
            // 失败任务
        }
        // 执行任务
        handlerMethod.invokeMethod(bizTask);
    }

    protected void handlerBizTaskExecException(BizTask task, Exception e) {

        BizTaskHelper.alarmTaskIfNecessity(task, e);

        BizTaskHelper.failOrDelayTask(task);

    }


}

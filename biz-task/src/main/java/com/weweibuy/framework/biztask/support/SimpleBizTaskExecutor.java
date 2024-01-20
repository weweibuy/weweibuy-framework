package com.weweibuy.framework.biztask.support;

import com.weweibuy.framework.biztask.core.BizTaskExecutor;
import com.weweibuy.framework.biztask.core.BizTaskHandlerMethodHolder;
import com.weweibuy.framework.biztask.db.po.BizTask;

import java.util.List;

/**
 * @author durenhao
 * @date 2024/1/20 14:02
 **/
public class SimpleBizTaskExecutor extends BizTaskExecutor {

    public SimpleBizTaskExecutor(BizTaskHandlerMethodHolder bizTaskHandlerMethodHolder) {
        super(bizTaskHandlerMethodHolder);
    }

    @Override
    protected void execTaskList(List<BizTask> bizTaskList) {
        bizTaskList.forEach(this::execTask);
    }
}

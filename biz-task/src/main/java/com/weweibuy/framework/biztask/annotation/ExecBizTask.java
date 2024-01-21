package com.weweibuy.framework.biztask.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 执行业务任务
 *
 * @author durenhao
 * @date 2024/1/20 11:22
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecBizTask {

    /**
     * 任务类型
     *
     * @return
     */
    String taskType();

    /**
     * 业务状态值
     *
     * @return
     */
    int bizStatus();


}

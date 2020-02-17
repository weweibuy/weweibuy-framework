package com.weweibuy.framework.compensate.config;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ExecutorService;

/**
 * @author durenhao
 * @date 2020/2/17 20:23
 **/
@Getter
@Setter
public class CompensateAsyncSupportConfigurer {

    /**
     * 保存补偿信息线程池
     */
    private ExecutorService saveInfoExecutorService;

    /**
     * 执行补偿线程池
     */
    private ExecutorService compensateExecutorService;




}

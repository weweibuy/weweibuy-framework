package com.weweibuy.framework.common.log.support;

import com.weweibuy.framework.common.log.config.LogDisablePath;

import java.util.List;

/**
 * 日志屏蔽配置
 *
 * @author durenhao
 * @date 2020/7/25 22:48
 **/
public interface LogDisableConfigurer {

    /**
     * 增加Http禁止输出配置
     */
    default void addHttpDisableConfig(List<LogDisablePath> disablePathList) {

    }


}

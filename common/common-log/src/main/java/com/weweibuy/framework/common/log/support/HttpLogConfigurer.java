package com.weweibuy.framework.common.log.support;

import com.weweibuy.framework.common.log.config.CommonLogProperties;

import java.util.List;

/**
 * 日志屏蔽配置
 *
 * @author durenhao
 * @date 2020/7/25 22:48
 **/
public interface HttpLogConfigurer {

    /**
     * 增加Http禁止输出配置
     */
    default void addHttpLogConfig(List<CommonLogProperties.CommonLogHttpProperties> logHttpProperties) {

    }


}

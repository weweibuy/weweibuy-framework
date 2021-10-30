package com.weweibuy.framework.common.feign.support;

import java.util.List;

/**
 * feign 日志配置
 *
 * @author durenhao
 * @date 2021/10/30 18:28
 **/
public interface FeignLogConfigurer {

    /**
     * 配置
     *
     * @param feignLogSettingList
     */
    default void configurer(List<FeignLogSetting> feignLogSettingList) {

    }

}

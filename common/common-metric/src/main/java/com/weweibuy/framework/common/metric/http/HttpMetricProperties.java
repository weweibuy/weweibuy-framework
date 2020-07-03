package com.weweibuy.framework.common.metric.http;

import lombok.Data;

import java.util.Map;

/**
 * Http 统计配置
 *
 * @author durenhao
 * @date 2020/7/3 21:25
 **/
@Data
public class HttpMetricProperties {

    /**
     * 名称前缀
     */
    private String namePrefix;

    /**
     * 路径名称映射
     */
    private Map<String, String> pathMapping;


}

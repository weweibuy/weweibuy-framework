package com.weweibuy.framework.common.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author durenhao
 * @date 2019/10/29 10:59
 **/
@ConfigurationProperties(prefix = "common.feign.http-client")
@Data
public class HttpClientProperties {

    private boolean useSSL = false;

    /**
     * 最大连接个数
     */
    private Integer maxTotal = 300;

    /**
     * 单host 最大连接个数
     */
    private Integer defaultMaxPerRoute = 50;

    /**
     * 连接超时
     */
    private Integer connectTimeout = 1000;

    /**
     * 获取连接超时
     */
    private Integer connectionRequestTimeout = 500;

    /**
     * 读取超时
     */
    private Integer socketTimeout = 3000;

    /**
     * 检查失效连接间隔  毫秒
     */
    private Integer checkExpiredConnectionInterval = 60000;

    /**
     * 超时是是否切换节点
     */
    private Boolean switchNodeWhenConnectionTimeout = false;

    /**
     * 连接的最大生命时间毫秒
     */
    private Long maxLifeTime = -1L;
}

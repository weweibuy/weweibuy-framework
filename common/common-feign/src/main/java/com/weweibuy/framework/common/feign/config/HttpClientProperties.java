package com.weweibuy.framework.common.feign.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author durenhao
 * @date 2019/10/29 10:59
 **/
@ConfigurationProperties(prefix = "common.feign.http-client")
@Data
public class HttpClientProperties {

    private boolean useSSL = true;

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


    /**
     * http 路径日志相关配置
     */
    private List<HttpClientProperties.LogHttpProperties> log;

    @Data
    public static class LogHttpProperties {

        /**
         * 请求host
         */
        private String host;

        /**
         * 请求路径
         */
        private String path;

        /**
         * 请求 method
         */
        private List<HttpMethod> methods;

        /**
         * 需要输出的请求头
         */
        private List<String> logReqHeader;

        /**
         * 需要输出的响应请头
         */
        private List<String> logRespHeader;

        /**
         * 是否禁止请求日志输出
         */
        private Boolean disableReq;

        /**
         * 是否禁止输出请求体
         */
        private Boolean disableReqBody;

        /**
         * 是否禁止响应日志输出
         */
        private Boolean disableResp;

        /**
         * 是否禁止输出响应日志体
         */
        private Boolean disableRespBody;


    }
}

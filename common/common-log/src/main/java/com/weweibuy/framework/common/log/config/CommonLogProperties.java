package com.weweibuy.framework.common.log.config;

import com.weweibuy.framework.common.core.utils.BeanCopyUtils;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author durenhao
 * @date 2020/6/24 23:34
 **/
@Data
@ConfigurationProperties(prefix = "common.log")
public class CommonLogProperties {

    /**
     * 是否启用 com.weweibuy.framework.common.log.config.CommonLogConfig 配置
     */
    private Boolean enable = true;

    /**
     * http 路径 相关配置
     */
    private List<HttpPathProperties> httpPath;

    @Data
    public static class HttpPathProperties {

        private static final Set<HttpMethod> SUPPORT_METHOD =
                Stream.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).collect(Collectors.toSet());
        /**
         * 请求路径
         */
        @NotBlank(message = "CommonLogHttpProperties.path 不能为空")
        private String path;

        /**
         * 请求 method
         */
        @NotEmpty(message = "CommonLogHttpProperties.method 不能为空")
        private Set<HttpMethod> methods = SUPPORT_METHOD;

        /**
         * 日志配置
         */
        private LogProperties log;

        /**
         * 脱敏相关配置
         */
        private SensitizationProperties sensitization;

    }

    @Data
    public static class LogProperties {
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
         * 禁止请求体日志输出
         */
        private Boolean disableReqBody;

        /**
         * 是否禁止响应日志输出
         */
        private Boolean disableResp;

        /**
         * 禁止响应体日志输出
         */
        private Boolean disableRespBody;
    }


    @Data
    public static class SensitizationProperties {

        /**
         * 敏感字段
         */
        private Set<String> sensitizationFields;

        /**
         * Logger 用于精确匹配
         */
        private String logger = HttpLogger.class.getName();
    }


}

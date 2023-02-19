package com.weweibuy.framework.common.log.config;

import com.weweibuy.framework.common.log.logger.HttpLogger;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Set;

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
     * http 路径日志相关配置
     */
    private List<CommonLogHttpProperties> httpPath;

    @Data
    public static class CommonLogHttpProperties {

        /**
         * 请求路径
         */
        @NotBlank(message = "CommonLogHttpProperties.path 不能为空")
        private String path;

        /**
         * 请求 method
         */
        @NotEmpty(message = "CommonLogHttpProperties.method 不能为空")
        private List<String> method;

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
         * 是否禁止响应日志输出
         */
        private Boolean disableResp;

        /**
         * 脱敏相关配置
         */
        private HttpSensitizationProperties sensitization;

    }

    @Data
    public static class HttpSensitizationProperties {

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

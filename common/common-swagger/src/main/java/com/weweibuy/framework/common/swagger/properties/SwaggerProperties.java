package com.weweibuy.framework.common.swagger.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/7/21 18:29
 **/
@Data
@ConfigurationProperties(prefix = "common.swagger")
public class SwaggerProperties {

    private String group = "swagger";

    private String version = "v1";

    private Set<String> basePackage = Collections.emptySet();

    private List<SwaggerHeaderProperties> headers = new ArrayList<>();

    private List<HttpStatusDescProperties> response = new ArrayList<>();

    @Data
    public static class HttpStatusDescProperties {

        private Integer status;

        private String desc;

    }

    @Data
    public static class SwaggerHeaderProperties {

        private String name = "";

        private String desc = "";

        private Boolean required = false;

    }


}

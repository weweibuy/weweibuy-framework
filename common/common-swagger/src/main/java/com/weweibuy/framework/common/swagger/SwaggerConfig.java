package com.weweibuy.framework.common.swagger;

import com.weweibuy.framework.common.swagger.properties.SwaggerProperties;
import com.weweibuy.framework.common.swagger.support.CustomBasePackageSelectors;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * swagger 配置, 只在dev环境生效
 *
 * @author durenhao
 * @date 2020/7/13 18:26
 **/
@AutoConfiguration
@Profile(value = {"dev"})
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    private final Environment environment;

    public SwaggerConfig(SwaggerProperties swaggerProperties, Environment environment) {
        this.swaggerProperties = swaggerProperties;
        this.environment = environment;
    }

    @Bean
    public Docket createRestApi() {

        List<RequestParameter> parameters = swaggerProperties.getHeaders().stream()
                .map(this::buildParameter)
                .collect(Collectors.toList());

        List<Response> responseMessageList = swaggerProperties.getResponse().stream()
                .map(this::buildResponseMessage)
                .collect(Collectors.toList());

        ApiSelectorBuilder select = new Docket(DocumentationType.OAS_30)

                .select();
        if (!CollectionUtils.isEmpty(swaggerProperties.getBasePackage())) {
            select.apis(CustomBasePackageSelectors.basePackage(swaggerProperties.getBasePackage()));
        }
        return select.paths(PathSelectors.any())
                .build();
    }


    private Response buildResponseMessage(SwaggerProperties.HttpStatusDescProperties statusDescProperties) {
        return new ResponseBuilder()
                .code(statusDescProperties.getStatus() + "")
                .build();
    }

    private RequestParameter buildParameter(SwaggerProperties.SwaggerHeaderProperties headerProperties) {
        RequestParameter build = new RequestParameterBuilder()
                .name(headerProperties.getName())
                .description(headerProperties.getDesc())
                .required(headerProperties.getRequired())
                .build();
        return build;
    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title(environment.getProperty("spring.application.name") + " 项目 Restful Apis")
                //版本号
                .version(swaggerProperties.getVersion())
                .build();
    }


}

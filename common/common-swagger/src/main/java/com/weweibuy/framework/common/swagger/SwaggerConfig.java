package com.weweibuy.framework.common.swagger;

import com.weweibuy.framework.common.swagger.properties.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
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
@Configuration
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

        List<Parameter> parameters = swaggerProperties.getHeaders().stream()
                .map(this::buildParameter)
                .collect(Collectors.toList());

        List<ResponseMessage> responseMessageList = swaggerProperties.getResponse().stream()
                .map(this::buildResponseMessage)
                .collect(Collectors.toList());

        ApiSelectorBuilder select = new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .enable(true)
                .globalOperationParameters(parameters)
                .groupName(swaggerProperties.getGroup())
                .apiInfo(apiInfo())
                .select();
        swaggerProperties.getBasePackage().forEach(p -> select.apis(RequestHandlerSelectors.basePackage(p)));
        return select.paths(PathSelectors.any())
                .build();
    }


    private ResponseMessage buildResponseMessage(SwaggerProperties.HttpStatusDescProperties statusDescProperties) {
        return new ResponseMessageBuilder()
                .code(statusDescProperties.getStatus())
                .message(statusDescProperties.getDesc())
                .responseModel(new ModelRef("CommonCodeResponse"))
                .build();
    }

    private Parameter buildParameter(SwaggerProperties.SwaggerHeaderProperties headerProperties) {
        Parameter build = new ParameterBuilder().name(headerProperties.getName())
                .description(headerProperties.getDesc())
                .modelRef(new ModelRef("String"))
                .parameterType("header")
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

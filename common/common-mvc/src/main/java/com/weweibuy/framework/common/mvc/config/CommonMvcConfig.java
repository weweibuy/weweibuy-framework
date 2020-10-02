package com.weweibuy.framework.common.mvc.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.support.SystemIdGetter;
import com.weweibuy.framework.common.mvc.advice.CommonErrorAttributes;
import com.weweibuy.framework.common.mvc.advice.CommonExceptionAdvice;
import com.weweibuy.framework.common.mvc.advice.FeignExceptionAdvice;
import com.weweibuy.framework.common.mvc.advice.FeignMethodKeyMappingConverter;
import com.weweibuy.framework.common.mvc.endpoint.CustomHealthController;
import com.weweibuy.framework.common.mvc.support.DefaultFeignExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author durenhao
 * @date 2020/3/2 17:53
 **/
@Configuration
public class CommonMvcConfig {

    @Autowired(required = false)
    private SystemIdGetter systemIdGetter;

    @Autowired(required = false)
    private List<FeignMethodKeyMappingConverter> feignMethodKeyMappingConverterList;


    /**
     * 注意顺序必须在  {@link CommonMvcConfig#commonExceptionAdvice() 之前}
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(name = {"feign.Feign", "com.weweibuy.framework.common.core.exception.MethodKeyFeignException"})
    public FeignExceptionAdvice feignExceptionAdvice() {
        return new FeignExceptionAdvice();
    }

    @Bean
    public CommonExceptionAdvice commonExceptionAdvice() {
        return new CommonExceptionAdvice();
    }

    @Bean
    public CustomHealthController customHealthController(){
        return new CustomHealthController();
    }


    @Bean
    @Primary
    public CommonErrorAttributes commonErrorAttributes() {
        return new CommonErrorAttributes(systemIdGetter);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeJackson2ObjectMapperBuilderCustomizer() {
        return builder ->
                builder.serializerByType(LocalDateTime.class, localDateTimeSerializer())
                        .deserializerByType(LocalDateTime.class, localDateTimeDeserializer());

    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateJackson2ObjectMapperBuilderCustomizer() {
        return builder ->
                builder.serializerByType(LocalDate.class, localDateSerializer())
                        .deserializerByType(LocalDate.class, localDateDeserializer());
    }

    @Bean
    @ConditionalOnClass(name = {"feign.Feign", "com.weweibuy.framework.common.core.exception.MethodKeyFeignException"})
    public DefaultFeignExceptionHandler defaultFeignExceptionHandler() {
        return new DefaultFeignExceptionHandler(feignMethodKeyMappingConverterList);
    }

    @Bean
    public HealthLogDisableConfigurer healthLogDisableConfigurer() {
        return new HealthLogDisableConfigurer();
    }


    public LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
    }

    public LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
    }

    public LocalDateSerializer localDateSerializer() {
        return new LocalDateSerializer(CommonConstant.DateConstant.STANDARD_DATE_FORMATTER);
    }

    public LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer(CommonConstant.DateConstant.STANDARD_DATE_FORMATTER);
    }
}

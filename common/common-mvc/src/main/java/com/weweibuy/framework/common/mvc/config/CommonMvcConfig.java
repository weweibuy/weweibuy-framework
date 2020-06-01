package com.weweibuy.framework.common.mvc.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.mvc.advice.CommonErrorAttributes;
import com.weweibuy.framework.common.mvc.advice.CommonExceptionAdvice;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2020/3/2 17:53
 **/
@Configuration
public class CommonMvcConfig   {


    @Bean
    public CommonExceptionAdvice commonExceptionAdvice() {
        return new CommonExceptionAdvice();
    }

    @Bean
    @Primary
    public CommonErrorAttributes commonErrorAttributes(){
        return new CommonErrorAttributes();
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

package com.weweibuy.framework.common.core.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.weweibuy.framework.common.core.model.constant.CommonConstant;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2020/3/1 10:57
 **/
@Configuration
public class JackJsonConfig {


    @Bean
    public JackJsonUtils jackJsonUtils() {
        return new JackJsonUtils();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeJackson2ObjectMapperBuilderCustomizer() {
        return localDateTimeCustomizer();

    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateJackson2ObjectMapperBuilderCustomizer() {
        return localDateCustomizer();
    }


    public static Jackson2ObjectMapperBuilderCustomizer localDateTimeCustomizer() {
        return builder ->
                builder.serializerByType(LocalDateTime.class, localDateTimeSerializer())
                        .deserializerByType(LocalDateTime.class, localDateTimeDeserializer());
    }


    public static Jackson2ObjectMapperBuilderCustomizer localDateCustomizer() {
        return builder ->
                builder.serializerByType(LocalDate.class, localDateSerializer())
                        .deserializerByType(LocalDate.class, localDateDeserializer());
    }

    private static LocalDateTimeSerializer localDateTimeSerializer() {
        return new LocalDateTimeSerializer(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
    }

    private static LocalDateTimeDeserializer localDateTimeDeserializer() {
        return new LocalDateTimeDeserializer(CommonConstant.DateConstant.STANDARD_DATE_TIME_FORMATTER);
    }

    private static LocalDateSerializer localDateSerializer() {
        return new LocalDateSerializer(CommonConstant.DateConstant.STANDARD_DATE_FORMATTER);
    }

    private static LocalDateDeserializer localDateDeserializer() {
        return new LocalDateDeserializer(CommonConstant.DateConstant.STANDARD_DATE_FORMATTER);
    }

}

package com.weweibuy.framework.common.core.config;

import com.weweibuy.framework.common.core.config.properties.CommonJacksonProperties;
import com.weweibuy.framework.common.core.support.JacksonBuilderHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TimeZone;

/**
 * jackson 配置
 *
 * @author durenhao
 * @date 2020/3/1 10:57
 **/
@EnableConfigurationProperties({CommonJacksonProperties.class})
@RequiredArgsConstructor
@AutoConfiguration
public class JackJsonConfig {

    private final CommonJacksonProperties commonJacksonProperties;


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeJackson2ObjectMapperBuilderCustomizer() {
        String format = commonJacksonProperties.getLocalDateTimeFormat();
        return builder ->
                builder.serializerByType(LocalDateTime.class,
                                JacksonBuilderHelper.localDateTimeSerializer(format))
                        .deserializerByType(LocalDateTime.class,
                                JacksonBuilderHelper.localDateTimeDeserializer(format))
                        .simpleDateFormat(format)
                        .timeZone(TimeZone.getDefault());
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateJackson2ObjectMapperBuilderCustomizer() {
        String format = commonJacksonProperties.getLocalDateFormat();
        return builder ->
                builder.serializerByType(LocalDate.class,
                                JacksonBuilderHelper.localDateSerializer(format))
                        .deserializerByType(LocalDate.class,
                                JacksonBuilderHelper.localDateDeserializer(format));
    }


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localTimeJackson2ObjectMapperBuilderCustomizer() {
        String format = commonJacksonProperties.getLocalTimeFormat();
        return builder ->
                builder.serializerByType(LocalTime.class,
                                JacksonBuilderHelper.localTimeSerializer(format))
                        .deserializerByType(LocalTime.class,
                                JacksonBuilderHelper.localTimeDeserializer(format));
    }


}

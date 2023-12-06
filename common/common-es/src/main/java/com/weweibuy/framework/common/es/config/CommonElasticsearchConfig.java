package com.weweibuy.framework.common.es.config;

import com.weweibuy.framework.common.es.converter.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * ES 配置;
 * 1. client ssl
 * 2. 字段风格
 * 3. 时间日期格式转化
 *
 * @author durenhao
 * @date 2022/12/28 22:40
 **/
@AutoConfiguration
public class CommonElasticsearchConfig extends ElasticsearchConfigurationSupport {


    @Bean
    public EsRestClientBuilderCustomizer esRestClientBuilderCustomizer(){
        return new EsRestClientBuilderCustomizer();
    }

    @Override
    protected FieldNamingStrategy fieldNamingStrategy() {
        return new SnakeCaseFieldNamingStrategy();
    }

    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        LocalDateTimeToStrConverter localDateTimeToStrConverter = new LocalDateTimeToStrConverter();
        StrToLocalDateTimeConverter strToLocalDateTimeConverter = new StrToLocalDateTimeConverter();

        LocalDateToStrConverter localDateToStrConverter = new LocalDateToStrConverter();
        StrToLocalDateConverter strToLocalDateConverter = new StrToLocalDateConverter();

        LocalTimeToStrConverter localTimeToStrConverter = new LocalTimeToStrConverter();
        StrToLocalTimeConverter strToLocalTimeConverter = new StrToLocalTimeConverter();

        List<Converter> converterList = new ArrayList<>();
        converterList.add(localDateTimeToStrConverter);
        converterList.add(localDateToStrConverter);
        converterList.add(localTimeToStrConverter);
        converterList.add(strToLocalDateTimeConverter);
        converterList.add(strToLocalDateConverter);
        converterList.add(strToLocalTimeConverter);
        return new ElasticsearchCustomConversions(converterList);
    }



}

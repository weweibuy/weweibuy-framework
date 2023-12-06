package com.weweibuy.framework.common.es.config;

import com.weweibuy.framework.common.es.converter.LocalDateTimeToStrConverter;
import com.weweibuy.framework.common.es.converter.LocalDateToStrConverter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * ES 配置
 * <p>
 * springBoot 2.3+ 版本使用的 ES 文档与Java对象的转化器是 {@link  MappingElasticsearchConverter}
 * 使用 {@link MappingElasticsearchConverter#readProperties(ElasticsearchPersistentEntity, Object, MappingElasticsearchConverter.ElasticsearchPropertyValueProvider)}
 * 进行转化
 * 自动配置 {@link ElasticsearchDataConfiguration.BaseConfiguration#elasticsearchConverter(SimpleElasticsearchMappingContext)}
 * <p>
 * <p>
 * springBoot 2.3 版本之前使用的是  EntityMapper
 *
 * @author durenhao
 * @date 2019/8/12 20:35
 **/
@AutoConfiguration
@RequiredArgsConstructor
public class CommonElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private final RestClientBuilder restClientBuilder;


    @Override
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(restClientBuilder);
    }

    @Override
    protected FieldNamingStrategy fieldNamingStrategy() {
        return new SnakeCaseFieldNamingStrategy();
    }

    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        LocalDateTimeToStrConverter localDateTimeToStrConverter = new LocalDateTimeToStrConverter();
        LocalDateToStrConverter localDateToStrConverter = new LocalDateToStrConverter();

        List<Converter> converterList = new ArrayList<>();
        converterList.add(localDateTimeToStrConverter);
        converterList.add(localDateToStrConverter);
        return new ElasticsearchCustomConversions(converterList);
    }
}

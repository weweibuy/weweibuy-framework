package com.weweibuy.framework.common.es.config;

import com.weweibuy.framework.common.es.convert.CustomerMappingElasticsearchConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

/**
 * ES 配置
 *
 * springBoot 2.3+ 版本使用的 ES 文档与Java对象的转化器是 {@link  MappingElasticsearchConverter}
 * 使用 {@link MappingElasticsearchConverter#readProperties(ElasticsearchPersistentEntity, Object, MappingElasticsearchConverter.ElasticsearchPropertyValueProvider)}
 * 进行转化
 * 自动配置 {@link ElasticsearchDataConfiguration.BaseConfiguration#elasticsearchConverter(SimpleElasticsearchMappingContext)}
 *
 * <p>
 * springBoot 2.3 版本之前使用的是  EntityMapper
 *
 * @author durenhao
 * @date 2019/8/12 20:35
 **/
//@Configuration
public class CommonElasticsearchConfig {

    @Bean
    public ElasticsearchConverter elasticsearchConverter(SimpleElasticsearchMappingContext mappingContext) {
        return new CustomerMappingElasticsearchConverter(mappingContext);
    }


}

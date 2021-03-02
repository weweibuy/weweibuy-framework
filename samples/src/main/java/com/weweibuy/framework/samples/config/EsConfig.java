/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.samples.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

/**
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
@Configuration
public class EsConfig {

//    @Bean
//    public EntityMapper entityMapper(SimpleElasticsearchMappingContext mappingContext, Jackson2ObjectMapperBuilder objectMapperBuilder) {
//        return new SnackCaseEsEntityMapper(mappingContext, objectMapperBuilder);
//    }


}

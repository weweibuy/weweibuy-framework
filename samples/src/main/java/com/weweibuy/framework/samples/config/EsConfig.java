/*
 * All rights Reserved, Designed By baowei
 *
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目的
 */
package com.weweibuy.framework.samples.config;

import com.weweibuy.framework.samples.es.SnackCaseEsEntityMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author durenhao
 * @date 2019/8/12 20:35
 **/
@Configuration
public class EsConfig {

    @Bean
    public EntityMapper entityMapper(SimpleElasticsearchMappingContext mappingContext, Jackson2ObjectMapperBuilder objectMapperBuilder) {
        return new SnackCaseEsEntityMapper(mappingContext, objectMapperBuilder);
    }



}

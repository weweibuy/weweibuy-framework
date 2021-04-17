package com.weweibuy.framework.common.es.convert;

import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

/**
 * @author durenhao
 * @date 2021/3/2 16:20
 **/
public class CustomerMappingElasticsearchConverter extends MappingElasticsearchConverter {

    private  ElasticsearchRestTemplate elasticsearchRestTemplate;

    public CustomerMappingElasticsearchConverter(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
        super(mappingContext);
    }




}

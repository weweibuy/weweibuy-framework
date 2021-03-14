package com.weweibuy.framework.common.es.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weweibuy.framework.common.core.utils.JackJsonUtils;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

import java.util.Map;

/**
 * @author durenhao
 * @date 2021/3/2 16:20
 **/
public class CustomerMappingElasticsearchConverter extends MappingElasticsearchConverter {

    private  ElasticsearchRestTemplate elasticsearchRestTemplate;

    public CustomerMappingElasticsearchConverter(MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext) {
        super(mappingContext);
    }

    @Override
    protected <R> R readProperties(Class<R> type, Map<String, Object> map) {
        ObjectMapper snakeCaseMapper = JackJsonUtils.getSnakeCaseMapper();
        return snakeCaseMapper.convertValue(map, type);
    }



}

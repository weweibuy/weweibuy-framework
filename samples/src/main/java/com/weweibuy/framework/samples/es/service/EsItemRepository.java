package com.weweibuy.framework.samples.es.service;

import com.weweibuy.framework.samples.es.model.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author durenhao
 * @date 2019/8/11 9:45
 **/
public interface EsItemRepository extends ElasticsearchRepository<Item, Long> {
}

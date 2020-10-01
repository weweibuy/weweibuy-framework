package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.samples.es.model.Item;
import com.weweibuy.framework.samples.es.service.EsItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author durenhao
 * @date 2020/10/1 21:43
 **/
@RestController
@RequestMapping("/es")
public class EsController {

    private final EsItemRepository esItemRepository;

    public EsController(EsItemRepository esItemRepository) {
        this.esItemRepository = esItemRepository;
    }

    @GetMapping("/query")
    public Object query(){

        PageRequest of = PageRequest.of(1, 10);

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withPageable(of);

        Page<Item> search = esItemRepository.search(queryBuilder.build());

        return search.getContent();
    }


}

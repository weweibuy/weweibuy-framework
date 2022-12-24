package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.utils.BeanCopyUtils;
import com.weweibuy.framework.samples.es.model.Item;
import com.weweibuy.framework.samples.es.service.EsItemRepository;
import com.weweibuy.framework.samples.mybatis.plugin.mapper.TbItemMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.TbItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author durenhao
 * @date 2020/10/1 21:43
 **/
@RestController
@RequestMapping("/es")
@RequiredArgsConstructor
public class EsController {

    private final EsItemRepository esItemRepository;

    private final TbItemMapper tbItemMapper;

    private final ElasticsearchRestTemplate elasticsearchRestTemplate;


    @GetMapping("/query")
    public Object query() {
        PageRequest of = PageRequest.of(1, 10);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withPageable(of);
        SearchHits<Item> search = elasticsearchRestTemplate.search(queryBuilder.build(), Item.class);
        return search.getSearchHits();
    }


    @GetMapping("/sync")
    public CommonCodeResponse sync() {
        esItemRepository.deleteAll();
        List<TbItem> tbItems = tbItemMapper.selectByExample(null);
        List<Item> itemList = tbItems.stream()
                .map(i -> {
                    Item item = BeanCopyUtils.copy(i, Item.class);
                    item.setId(i.getId());
                    return item;
                })
                .collect(Collectors.toList());

        esItemRepository.saveAll(itemList);
        return CommonCodeResponse.success();
    }


}

package com.weweibuy.framework.samples.controller;

import com.weweibuy.framework.common.core.model.dto.CommonCodeResponse;
import com.weweibuy.framework.common.core.utils.BeanCopyUtils;
import com.weweibuy.framework.samples.es.model.Item;
import com.weweibuy.framework.samples.es.service.EsItemRepository;
import com.weweibuy.framework.samples.mybatis.plugin.mapper.TbItemMapper;
import com.weweibuy.framework.samples.mybatis.plugin.model.po.TbItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.BaseQuery;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final ElasticsearchTemplate elasticsearchTemplate;


    @GetMapping("/query")
    public Object query() {
        PageRequest of = PageRequest.of(1, 10);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withPageable(of);
        BaseQuery baseQuery = new BaseQuery();
        Criteria and = Criteria.and();
        CriteriaQueryBuilder criteriaQueryBuilder = new CriteriaQueryBuilder(and);
        criteriaQueryBuilder.withPageable(of);
        baseQuery.setPageable(of);

        SearchHits<Item> search = elasticsearchTemplate.search(criteriaQueryBuilder.build(), Item.class);
        return search.getSearchHits();
    }


    @GetMapping("/sync")
    public CommonCodeResponse sync() {
        esItemRepository.deleteAll();
        List<TbItem> tbItems = tbItemMapper.selectByExample(null);
        List<Item> itemList = tbItems.stream().map(i -> {
            Item item = BeanCopyUtils.copy(i, Item.class);
            item.setId(i.getId());
            return item;
        }).collect(Collectors.toList());

        esItemRepository.saveAll(itemList);
        return CommonCodeResponse.success();
    }

    public static void main(String[] args) {
        List<Integer> collect = Stream.iterate(0, n -> n + 1)
                .limit(1000000).collect(Collectors.toList());

        long l = System.currentTimeMillis();
        collect.forEach(i -> {
            int i1 = i + 1;
        });
        System.err.println(System.currentTimeMillis() - l);
        collect.parallelStream().forEachOrdered(i -> {
            int i1 = i + 1;
        });
        long l2 = System.currentTimeMillis();
        collect.parallelStream().forEachOrdered(i -> {
            int i1 = i + 1;
        });
        System.err.println(System.currentTimeMillis() - l2);

    }


}

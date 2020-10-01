package com.weweibuy.framework.samples.es.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(indexName = "learning_item", type = "_doc", createIndex = false)
public class Item {

    @JsonProperty("_id")
    private String esId;

    private Long id;

    private String title;

    private String sellPoint;

    private BigDecimal price;

    private Integer stockCount;

    private Integer num;

    private String barcode;

    private String image;

    private Long categoryid;

    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String itemSn;

    private BigDecimal costPirce;

    private BigDecimal marketPrice;

    private String isDefault;

    private Long goodsId;

    private String sellerId;

    private String cartThumbnail;

    private String category;

    private String brand;

    private String spec;

    private String seller;

}
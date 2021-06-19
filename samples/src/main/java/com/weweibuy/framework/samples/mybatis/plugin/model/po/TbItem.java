package com.weweibuy.framework.samples.mybatis.plugin.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TbItem {
    /**
     * 商品id，同时也是商品编号
     */
    private Long id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品卖点
     */
    private String sellPoint;

    /**
     * 商品价格，单位为：元
     */
    private BigDecimal price;

    /**
     */
    private Integer stockCount;

    /**
     * 库存数量
     */
    private Integer num;

    /**
     * 商品条形码
     */
    private String barcode;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 所属类目，叶子类目
     */
    private Long categoryid;

    /**
     * 商品状态，1-正常，2-下架，3-删除
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     */
    private String itemSn;

    /**
     */
    private BigDecimal costPirce;

    /**
     */
    private BigDecimal marketPrice;

    /**
     */
    private String isDefault;

    /**
     */
    private Long goodsId;

    /**
     */
    private String sellerId;

    /**
     */
    private String cartThumbnail;

    /**
     */
    private String category;

    /**
     */
    private String brand;

    /**
     */
    private String spec;

    /**
     */
    private String seller;
}
package com.weweibuy.framework.samples.state.biz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author : Knight
 * @date : 2020/10/18 10:12 下午
 */
@Getter
@Setter
@Builder
public class OrderDto {
    /**
     * id
     */
    private Long id;

    /**
     * 状态
     */
    private String orderStatus;

    /**
     * 单据号
     */
    private String orderNo;

    /**
     * 创建时间
     */
    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 更新时间
     */
    @Builder.Default
    private LocalDateTime updateTime = LocalDateTime.now();
}

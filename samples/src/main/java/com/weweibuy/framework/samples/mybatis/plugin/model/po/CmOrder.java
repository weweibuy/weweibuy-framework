package com.weweibuy.framework.samples.mybatis.plugin.model.po;

import com.weweibuy.framework.common.db.model.CommonPo;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CmOrder implements CommonPo, Serializable {
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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     */
    private Boolean deleted;
}
package com.weweibuy.framework.samples.mybatis.plugin.model.po;

import com.weweibuy.framework.common.db.model.CommonPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DBEncrypt implements CommonPo, Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * phone
     */
    private String phone;

    /**
     * id_no
     */
    private String idNo;

    /**
     * order_no
     */
    private String orderNo;

    /**
     * create_time
     */
    private LocalDateTime createTime;

    /**
     * update_time
     */
    private LocalDateTime updateTime;

    /**
     */
    private Boolean deleted;

    /**
     */
    private String tag;
}
package com.weweibuy.framework.samples.mybatis.plugin.model.po;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DBEncrypt {
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
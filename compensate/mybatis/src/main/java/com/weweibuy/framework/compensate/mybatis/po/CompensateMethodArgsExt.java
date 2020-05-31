package com.weweibuy.framework.compensate.mybatis.po;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CompensateMethodArgsExt {
    /**
     * id
     */
    private Long id;

    /**
     * 补偿id 关联补偿表
     */
    private Long compensateId;

    /**
     * 方法参数
     */
    private String methodArgs;

    /**
     * 参数排序(值越小越靠前)
     */
    private Integer argsOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
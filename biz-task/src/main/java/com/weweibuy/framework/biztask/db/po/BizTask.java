package com.weweibuy.framework.biztask.db.po;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class BizTask {
    /**
     * id
     */
    private Long id;

    /**
     * 业务id
     */
    private Long bizId;

    /**
     * 业务类型
     */
    private String taskType;

    /**
     * 业务参数
     */
    private String taskParam;

    /**
     * 触发次数
     */
    private Integer triggerCount;

    /**
     * 下次触发时间
     */
    private LocalDateTime nextTriggerTime;

    /**
     * 任务状态(1: 待执行; 2: 执行中;  3: 执行成功;  4: 执行失败)
     */
    private Integer taskStatus;

    /**
     * 业务状态
     */
    private Integer bizStatus;

    /**
     */
    private String remark;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
package com.weweibuy.framework.compensate.mybatis.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Compensate {
    /**
     * id
     */
    private Long id;

    /**
     * 补偿key
     */
    private String compensateKey;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 补偿类型
     */
    private String compensateType;

    /**
     * 方法参数
     */
    private String methodArgs;

    /**
     * 下次触发时间
     */
    private LocalDateTime nextTriggerTime;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 补偿状态: 0:补偿中, 1:失败; 2: 成功
     */
    private Byte compensateStatus;

    /**
     * 告警次数
     */
    private Integer alarmCount;

    /**
     * 是否有扩展参数(当method_args存储长度不足时,将吧多余的数据用compensate_method_args_ext表存储)
     */
    private Boolean hasArgsExt;

    /**
     * 是否删除
     */
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
package com.weweibuy.framework.compensate.mybatis.po;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CompensateLog {
    /**
     * id
     */
    private Long id;

    /**
     * 补偿id 关联补偿表
     */
    private Long compensateId;

    /**
     * 触发方式(SYSTEM:系统触发; FORCE:人工强制触发 )
     */
    private String triggerType;

    /**
     * 补偿状态(RETRY_ABLE:可以重试; ALARM_ABLE:可以报警; OVER_ALARM_COUNT:超出报警次数)
     */
    private String compensateState;

    /**
     * 补偿结果: (RETRY_FAIL:补偿重试失败; RETRY_FAIL_RECOVER:补偿重试失败,触发恢复方法; RETRY_SUCCESS:补偿重试成功; ALARM:报警; OVER_ALARM_COUNT:超出报警上限)
     */
    private String compensateResult;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
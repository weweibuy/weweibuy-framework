package com.weweibuy.framework.compensate.core;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author durenhao
 * @date 2020/2/13 20:09
 **/
@Data
public class CompensateInfo {

    private String compensateKey;

    private String bizId;

    private String args;

    /**
     * 下一次触发时间
     */
    private LocalDateTime nextTriggerTime;

}

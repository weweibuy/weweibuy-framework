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

    private Integer retryCount = 0;

    private Integer alarmCount = 0;

    private LocalDateTime updateTime = LocalDateTime.now();

}

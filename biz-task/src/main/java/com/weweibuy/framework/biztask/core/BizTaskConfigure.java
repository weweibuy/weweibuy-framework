package com.weweibuy.framework.biztask.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author durenhao
 * @date 2024/1/21 10:40
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BizTaskConfigure {

    /**
     * 配置
     *
     */
    private String rule;

    /**
     * 对多执行次数
     */
    private Integer maxExecCount;

    /**
     * 执行N次,未成功后报警
     */
    private Integer alarmAtCount;


}

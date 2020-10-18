package com.weweibuy.framerwork.statemachine.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Knight
 * @date : 2020/10/18 3:08 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultHolder {
    /**
     * 状态机执行结果
     */
    private Object resultData;
}

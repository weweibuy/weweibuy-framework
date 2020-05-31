package com.weweibuy.framework.compensate.mybatis.constant;

/**
 * 补偿状态常量
 *
 * @author durenhao
 * @date 2020/5/31 22:16
 **/
public interface CompensateStatusConstant {

    /**
     * 补偿中
     */
    Byte COMPENSATING = 0;

    /**
     * 补偿失败
     */
    Byte COMPENSATE_FAIL = 1;

    /**
     * 补偿成功
     */
    Byte COMPENSATE_SUCCESS = 2;


}

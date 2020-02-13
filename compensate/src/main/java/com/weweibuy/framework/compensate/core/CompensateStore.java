package com.weweibuy.framework.compensate.core;

/**
 * 补偿存储器
 *
 * @author durenhao
 * @date 2020/2/13 20:02
 **/
public interface CompensateStore {

    /**
     * 保存补偿信息
     *
     * @param compensateInfo
     */
    void saveCompensateInfo(CompensateInfo compensateInfo);

    /**
     * 查询补偿信息
     *
     * @return
     */
    Object[] queryCompensateInfo();

}

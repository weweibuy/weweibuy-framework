package com.weweibuy.framework.compensate.core;

/**
 * 业务id 组装补偿参数
 *
 * @author durenhao
 * @date 2020/2/16 12:14
 **/
public interface BizIdCompensateAssemble {

    /**
     * 根据  compensateKey 业务id 组装补偿参数
     *
     * @param compensateKey
     * @param bizId
     * @return
     */
    Object[] assemble(String compensateKey, String bizId);

}

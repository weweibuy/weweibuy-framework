package com.weweibuy.framework.compensate.core;

/**
 * 补偿配置存储
 *
 * @author durenhao
 * @date 2020/2/13 20:42
 **/
public interface CompensateConfigStore {

    /**
     * 根据Key 获取对应配置
     *
     * @param compensateKey
     * @return
     */
    CompensateConfigProperties compensateConfig(String compensateKey);

}

package com.weweibuy.framework.compensate.support;


import com.weweibuy.framework.compensate.interfaces.model.CompensateInfo;

/**
 * 方法参数转化
 *
 * @author durenhao
 * @date 2020/2/14 22:29
 **/
public interface MethodArgsConverter {

    /**
     * 法法参数转 string  便于存储
     *
     * @param args
     * @return
     */
    String convert(String compensateKey, Object[] args);

    /**
     * String 转方法参数
     *
     * @param compensateInfo
     * @return
     */
    Object[] parser(CompensateInfo compensateInfo);
}

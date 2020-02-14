package com.weweibuy.framework.compensate.support;

/**
 * 方法参数转化
 *
 * @author durenhao
 * @date 2020/2/14 22:29
 **/
public interface MethodArgsWrapperConverter {

    /**
     * 法法参数转 string  便于存储
     *
     * @param methodArgs
     * @return
     */
    String convert(MethodArgsWrapper methodArgs);

    /**
     * String 转方法参数
     *
     * @param str
     * @return
     */
    MethodArgsWrapper parser(String str);
}

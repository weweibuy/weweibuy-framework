package com.weweibuy.framework.compensate.support;

/**
 * 恢复方法参数解析器
 *
 * @author durenhao
 * @date 2020/2/15 22:17
 **/
public interface RecoverMethodArgsResolver {

    /**
     * 是否匹配
     *
     * @param compensateKey
     * @return
     */
    boolean match(String compensateKey);

    /**
     * 解析参数
     *
     * @param returnArg
     * @param inputArg
     * @return
     */
    Object[] resolver(Object returnArg, Object[] inputArg);

}

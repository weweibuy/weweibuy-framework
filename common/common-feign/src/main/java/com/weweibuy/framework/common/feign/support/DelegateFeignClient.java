package com.weweibuy.framework.common.feign.support;

import feign.Client;

/**
 * 代理 feignClient
 * <p>
 * 可以做请求/响应的拦截处理
 *
 * @author durenhao
 * @date 2021/10/30 16:49
 **/
public interface DelegateFeignClient {


    /**
     * 代理
     *
     * @param client
     * @return
     */
    Client delegate(Client client);

}

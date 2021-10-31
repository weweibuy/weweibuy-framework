package com.weweibuy.framework.common.feign.support;

import feign.Client;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * 代理 feignClient
 * <p>
 * 可以做请求/响应的拦截处理, 或 mock
 * 多个实例形成链式代理  使用 {@link Order} 进行排序
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

    /**
     * 代理链
     *
     * @param delegateFeignClientListList
     * @param client
     * @return
     */
    static Client delegateChain(List<DelegateFeignClient> delegateFeignClientListList, Client client) {
        if (CollectionUtils.isNotEmpty(delegateFeignClientListList)) {
            for (int i = delegateFeignClientListList.size() - 1; i >= 0; i--) {
                client = delegateFeignClientListList.get(i).delegate(client);
            }
        }
        return client;
    }

}

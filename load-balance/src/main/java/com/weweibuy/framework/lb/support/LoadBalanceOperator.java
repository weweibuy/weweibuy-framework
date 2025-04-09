package com.weweibuy.framework.lb.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 操作 lb
 *
 * @author durenhao
 * @date 2020/9/25 11:50
 **/
@Slf4j
public class LoadBalanceOperator {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private LoadBalancerClientFactory loadBalancerClientFactory;


    public List<ServiceInstance> listServer(String name) {
        Set<String> contextNames = loadBalancerClientFactory.getContextNames();
        if (!contextNames.contains(name)) {
            return Collections.emptyList();
        }
        return loadBalancerClientFactory.getInstance(name, ServiceInstanceListSupplier.class).get()
                .reduce(new ArrayList<ServiceInstance>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                })
                .block();
    }

    public Map<String, List<ServiceInstance>> allServerMap() {
        Set<String> contextNames = loadBalancerClientFactory.getContextNames();

        return contextNames.stream()
                .collect(Collectors.toMap(Function.identity(),
                        this::listServer));
    }

    public void update(String name) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        Set<String> contextNames = loadBalancerClientFactory.getContextNames();
        if (!contextNames.contains(name)) {
            log.warn("服务: {}, 不存在,无需更新");
            return;
        }
        log.info("接受到服务: {} 变更消息", name);

        LoadBalancerCacheManager cacheManager = applicationContext.getBean(LoadBalancerCacheManager.class);

        Collection<String> cacheNames = cacheManager.getCacheNames();

        cacheNames.stream()
                .map(cacheManager::getCache)
                .filter(cache -> Objects.nonNull(cache.get(name)))
                .peek(cache -> cache.evict(name))
                .peek(cache -> listServer(name))
                .forEach(cache -> log.info("更新服务: {} 信息成功", name));
    }

    public void update() {
        Set<String> contextNames = loadBalancerClientFactory.getContextNames();
        contextNames.forEach(this::update);
    }


}

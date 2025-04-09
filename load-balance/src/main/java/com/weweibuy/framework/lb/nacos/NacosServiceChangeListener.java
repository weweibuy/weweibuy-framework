package com.weweibuy.framework.lb.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.weweibuy.framework.lb.support.LoadBalanceOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 订阅nacos 服务变更, 更新本地服务信息
 *
 * @date 2025/3/29
 **/
@Slf4j
public class NacosServiceChangeListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private LoadBalanceOperator loadBalanceOperator;

    @Autowired
    private NacosDiscoveryProperties properties;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    private static final String NAME_PREFIX = LoadBalancerClientFactory.class.getSimpleName() + "-";

    private final Set<String> listenedApp = new HashSet<>();

    // TODO 如何获取到要订阅的服务?
    public void addListener(String serviceName) throws NacosException {

        NamingService naming = nacosServiceManager
                .getNamingService(properties.getNacosProperties());

        EventListener serviceListener = createListener();


        naming.subscribe(serviceName, properties.getGroup(),
                Arrays.asList(properties.getClusterName()), serviceListener);
    }


    private EventListener createListener() {
        return event -> {
            if (event instanceof NamingEvent) {
                String name = ((NamingEvent) event).getServiceName();
                int index = -1;
                if ((index = name.indexOf("@@")) != -1) {
                    String appName = name.substring(index + 2);
                    // 更新服务
                    loadBalanceOperator.update(appName);
                }

            }
        };
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Object source = event.getSource();
        if (source instanceof AnnotationConfigApplicationContext) {
            String displayName = ((AnnotationConfigApplicationContext) source).getDisplayName();
            if (displayName.startsWith(NAME_PREFIX)) {
                String appName = displayName.substring(NAME_PREFIX.length());
                if (!listenedApp.contains(appName)) {
                    try {
                        addListener(appName);
                    } catch (NacosException e) {
                        log.warn("订阅nacos服务: {} 变更失败: ", appName, e);
                        return;
                    }
                    listenedApp.add(appName);
                }
            }
        }
    }
}

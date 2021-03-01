package com.weweibuy.framework.lb.support;

import lombok.extern.slf4j.Slf4j;

/**
 * 操作 lb
 *
 * @author durenhao
 * @date 2020/9/25 11:50
 **/
@Slf4j
public class LoadBalanceOperator {

//    @Autowired
//    private SpringClientFactory springClientFactory;
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//
//    public List<Server> listServer(String name) {
//        return Optional.ofNullable(springClientFactory.getLoadBalancer(name))
//                .map(ILoadBalancer::getAllServers)
//                .orElse(Collections.emptyList());
//    }
//
//    public Map<String, List<Server>> allServerMap() {
//        return springClientFactory.getContextNames().stream()
//                .collect(Collectors.toMap(Function.identity(),
//                        name -> springClientFactory.getLoadBalancer(name)
//                                .getAllServers()));
//
//    }
//
//    public void update(String name) {
//        if (StringUtils.isBlank(name)) {
//            return;
//        }
//        log.info("接受到服务: {} 变更消息", name);
//        ILoadBalancer loadBalancer = springClientFactory.getLoadBalancer(name);
//        if (loadBalancer instanceof DynamicServerListLoadBalancer) {
//            DynamicServerListLoadBalancer dynamicServerListLoadBalancer = (DynamicServerListLoadBalancer) loadBalancer;
//            dynamicServerListLoadBalancer.updateListOfServers();
//            log.info("更新服务信息成功");
//        }
//    }
//
//    private void update() {
//        springClientFactory.getContextNames().stream()
//                .forEach(this::update);
//    }


}

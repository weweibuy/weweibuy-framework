package com.weweibuy.framework.lb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author durenhao
 * @date 2020/9/24 22:50
 **/
@Data
@ConfigurationProperties(prefix = "common.lb")
public class LoadBalanceProperties {

    private ServerChangeListener serverChangeListener = new ServerChangeListener();


    /**
     * 感兴趣的服务
     */
    private Set<String> interestingServers = new HashSet<>();

    @Data
    public static class ServerChangeListener {

        private String topic;

        private String group;

    }

}

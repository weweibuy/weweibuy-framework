package com.weweibuy.framework.rocketmq.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * rocketMq相关工具
 *
 * @author durenhao
 * @date 2020/6/21 17:45
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RocketMqUtils {

    public static boolean canUseAcl(String accessKey, String secretKey) {
        boolean userAcl = !StringUtils.isAnyBlank(accessKey, secretKey);
        if (!userAcl) {
            return false;
        }
        boolean hasSdk = true;
        try {
            Class<?> clazz = Class.forName("org.apache.rocketmq.acl.common.AclClientRPCHook");
        } catch (ClassNotFoundException e) {
            log.warn("没有发现 rocketMq ACL 的SDK");
            hasSdk = false;
        }
        return userAcl && hasSdk;
    }


}

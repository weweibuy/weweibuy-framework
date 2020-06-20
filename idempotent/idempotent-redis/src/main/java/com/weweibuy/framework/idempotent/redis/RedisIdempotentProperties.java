package com.weweibuy.framework.idempotent.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author durenhao
 * @date 2020/6/20 11:21
 **/
@Data
@ConfigurationProperties(prefix = "idempotent.redis")
public class RedisIdempotentProperties {

    private String keyPrefix = "idem:";

}

package com.weweibuy.framework.common.db.properties;

import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author durenhao
 * @date 2020/9/6 0:03
 **/
@Data
@ConfigurationProperties(prefix = "db.encrypt")
public class DBEncryptProperties {

    private List<Key> key;

    @Data
    public static class Key {

        private String num;

        private String password;

        private Boolean enable = true;

    }


    public void validate() {
        if (CollectionUtils.isEmpty(key)) {
            throw Exceptions.system("数据库加密, 秘钥配置错误");
        }
        boolean match = key.stream()
                .anyMatch(k -> StringUtils.isBlank(k.getPassword()) ||
                        !k.getNum().matches("^[a-z0-9A-Z][a-z0-9A-Z]$"));
        if (match) {
            throw Exceptions.system("数据库加密, 秘钥配置错误");
        }

    }

}

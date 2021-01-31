package com.weweibuy.framework.common.db.properties;

import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 加密配置, 注意默认秘钥长度只支持 128 bit位(16长度字符串)
 *
 * @author durenhao
 * @date 2020/9/6 0:03
 **/
@Data
@ConfigurationProperties(prefix = "common.db.encrypt")
@ConditionalOnProperty(name = "common.db.encrypt.enable", havingValue = "true")
public class DBEncryptProperties {

    private Boolean enable = false;

    /**
     * AES 加密密码 与 passwordFile 二选一
     */
    private String password;

    /**
     * AES 加密密码文件  与 password 二选一;
     * <p>
     * eg: classpath:db-encrypt.key  or
     * <p>
     * db-encrypt.key
     * or
     * AbsolutePath
     */
    private String passwordFile;


    public void validate() {
        if (StringUtils.isBlank(password) && StringUtils.isBlank(passwordFile)) {
            throw Exceptions.system("数据库数据加密,密码或密码文件地址不能为空");
        }
        if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(passwordFile)) {
            throw Exceptions.system("数据库数据加密,密码或密码文件地址只能配置一个");
        }

    }


}

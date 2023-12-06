package com.weweibuy.framework.common.db.encrypt;

import com.weweibuy.framework.common.codec.aes.AESUtils;
import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.ClassPathFileUtils;
import com.weweibuy.framework.common.db.properties.DBEncryptProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Optional;

/**
 * AES 加密
 *
 * @author durenhao
 * @date 2020/9/5 12:10
 **/
@Component
public class AESEncryptHelper {

    @Autowired
    private DBEncryptProperties dbEncryptProperties;

    private static SecretKey secretKey;

    private static Boolean enable = false;


    @PostConstruct
    public void init() {
        dbEncryptProperties.validate();
        enable = dbEncryptProperties.getEnable();
        String password = Optional.ofNullable(dbEncryptProperties.getPassword())
                .filter(StringUtils::isNotBlank)
                .orElseGet(() -> Optional.ofNullable(dbEncryptProperties.getPasswordFile())
                        .filter(StringUtils::isNotBlank)
                        .map(ClassPathFileUtils::classPathFileContentOrOther)
                        .orElse(null));
        if (enable && StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("开启数据库加密后没有配置秘钥");
        }
        secretKey = AESUtils.createKey(password);
    }


    public static String encrypt(String parameter) {
        try {
            return AESUtils.encryptToHex(secretKey, parameter);
        } catch (Exception e) {
            throw Exceptions.system("数据库加密,异常", e);
        }
    }


    public static boolean isEncrypted(String r) {
        return enable && StringUtils.isNotBlank(r) && r.length() >= 32;
    }

    public static String decrypt(String r) {
        try {
            return AESUtils.decryptHex(secretKey, r);
        } catch (Exception e) {
            throw Exceptions.system("数据库解密,异常", e);
        }
    }

}

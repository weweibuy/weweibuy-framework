package com.weweibuy.framework.common.db.encrypt;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.AESUtils;
import com.weweibuy.framework.common.db.properties.DBEncryptProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

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

    private static DBEncryptProperties encryptProperties;

    private static List<DBEncryptProperties.Key> enableList;

    private static Map<String, SecretKey> secretKeyMap;

    private static final String CONNECTOR_STR = "-";


    @PostConstruct
    public void init() {
        dbEncryptProperties.validate();
        encryptProperties = dbEncryptProperties;

        enableList = encryptProperties.getKey().stream()
                .filter(DBEncryptProperties.Key::getEnable)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(enableList)) {
            throw Exceptions.system("数据库加密,没有可用秘钥");
        }
        secretKeyMap = encryptProperties.getKey().stream()
                .collect(Collectors.toMap(DBEncryptProperties.Key::getNum,
                        key -> AESUtils.createKey(key.getPassword())));
    }


    public static String encrypt(String parameter) {
        DBEncryptProperties.Key key = null;
        if (enableList.size() == 1) {
            key = enableList.get(0);
        } else {
            key = enableList.get(RandomUtils.nextInt(0, enableList.size() + 1));
        }
        String num = key.getNum();
        SecretKey secretKey = secretKeyMap.get(num);
        try {
            return compose(num, AESUtils.encrypt(secretKey, parameter));
        } catch (Exception e) {
            throw Exceptions.system("数据库加密,异常", e);
        }
    }


    private static String compose(String key, String encrypt) {
        return key + "-" + encrypt;
    }


    public static boolean isEncrypted(String r) {
        return StringUtils.isNotBlank(r) && r.length() > 32 && r.indexOf(CONNECTOR_STR) == 2;
    }

    public static String decrypt(String r) {
        String[] split = r.split(CONNECTOR_STR, 2);
        String key = split[0];
        String encryptContent = split[1];
        SecretKey secretKey = secretKeyMap.get(key);
        try {
            return AESUtils.decrypt(secretKey, encryptContent);
        } catch (Exception e) {
            throw Exceptions.system("数据库解密,异常", e);
        }
    }

}

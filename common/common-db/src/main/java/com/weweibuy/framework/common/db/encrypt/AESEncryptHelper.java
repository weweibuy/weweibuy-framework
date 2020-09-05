package com.weweibuy.framework.common.db.encrypt;

import com.weweibuy.framework.common.core.exception.Exceptions;
import com.weweibuy.framework.common.core.utils.AESUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import java.util.*;

/**
 * AES 加密
 *
 * @author durenhao
 * @date 2020/9/5 12:10
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AESEncryptHelper {

    private static final String PROPERTIES = "config/db-aes-key";

    private static final String PROPERTIES_KEY = "db.encrypt.key";

    private static final String CONNECTOR_STR = "-";

    private static Map<String, SecretKey> keyMap;

    private static List<String> keyList = new ArrayList<>();


    static {
        keyMap = loadProperties();
    }

    private static Map<String, SecretKey> loadProperties() {
        ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES);
        String baseBundleName = bundle.getBaseBundleName();
        Enumeration<String> keys = bundle.getKeys();
        Map<String, SecretKey> hashMap = new HashMap();
        while (keys.hasMoreElements()) {
            String element = keys.nextElement();
            if (element.indexOf(PROPERTIES_KEY) == -1) {
                throw Exceptions.system("数据库加密,秘钥配置错误!");
            }
            String substring = StringUtils.substring(element, 15, element.length());
            if (StringUtils.isBlank(substring) || substring.length() > 1 || !substring.matches("^[a-z0-9A-Z]$")) {
                throw Exceptions.system("数据库加密,秘钥编号配置错误!");
            }
            SecretKey secretKey = hashMap.get(substring);
            if (secretKey != null) {
                throw Exceptions.system("数据库加密,秘钥编号重复");
            }
            hashMap.put(substring,
                    AESUtils.createKey(bundle.getString(element)));
            keyList.add(substring);
        }
        if (hashMap.isEmpty()) {
            throw Exceptions.system("数据库加密,没有秘钥信息");
        }
        return hashMap;

    }


    public static String encrypt(String parameter) {
        int anInt = RandomUtils.nextInt(0, keyList.size());
        String key = keyList.get(anInt);
        SecretKey secretKey = keyMap.get(key);
        try {
            return compose(key, AESUtils.encrypt(secretKey, parameter));
        } catch (Exception e) {
            throw Exceptions.system("数据库加密,异常", e);
        }
    }

    private static String compose(String key, String encrypt) {
        return key + "-" + encrypt;
    }


    public static boolean isEncrypted(String r) {
        return StringUtils.isNotBlank(r) && r.length() > 32 && r.indexOf(CONNECTOR_STR) == 1;
    }

    public static String decrypt(String r) {
        String key = r.substring(0, 1);
        String encryptContent = r.substring(2, r.length());
        SecretKey secretKey = keyMap.get(key);
        try {
            return AESUtils.decrypt(secretKey, encryptContent);
        } catch (Exception e) {
            throw Exceptions.system("数据库解密,异常", e);
        }
    }

}

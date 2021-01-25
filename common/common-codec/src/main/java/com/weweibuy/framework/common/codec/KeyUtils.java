package com.weweibuy.framework.common.codec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author durenhao
 * @date 2021/1/25 21:13
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyUtils {


    public static SecretKey generalHS256Key(String secret) {
        byte[] stringKey = secret.getBytes();
        byte[] encodedKey = Base64.getEncoder().encode(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "HS256");
        return key;
    }

}

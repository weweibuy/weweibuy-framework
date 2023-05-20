package com.weweibuy.framework.common.codec.aes;

import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Aes256UtilsTest {

    @Test
    public void generateKeyHex() {
        String key = Aes256GcmUtils.generateKeyHex();
        String substring = key.substring(0, 32);
        SecretKey secretKey =  new SecretKeySpec(substring.getBytes(), AESUtils.ALGORITHM);
        byte[] encoded = secretKey.getEncoded();
        System.err.println(key);
        System.err.println(key.length());
        SecretKey key1 = Aes256GcmUtils.secretKey(key);
    }
}
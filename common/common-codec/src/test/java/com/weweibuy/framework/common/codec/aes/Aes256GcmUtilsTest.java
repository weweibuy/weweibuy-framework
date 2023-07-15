package com.weweibuy.framework.common.codec.aes;

import org.junit.Test;

import javax.crypto.SecretKey;

public class Aes256GcmUtilsTest {

    @Test
    public void createKey() throws Exception{
        String str = "";
        for (int i = 0; i < 32; i++) {
            str += "v";
        }
        SecretKey key = Aes256GcmUtils.secretKey(str);
        String content = "123";
        String encryptHex = Aes256GcmUtils.encryptToHex(content, null, "1", key);
        System.err.println(encryptHex);
        System.err.println(encryptHex.length());
        String decryptContent = Aes256GcmUtils.decryptHexText(encryptHex, null, "1", key);
        System.err.println(decryptContent);

    }

    @Test
    public void createKey2() throws Exception{
        String str = "";
        for (int i = 0; i < 32; i++) {
            str += "v";
        }
        SecretKey key = Aes256GcmUtils.secretKey(str);
        String content = "123";
        String encryptHex = Aes256GcmUtils.encryptToBase64(content, null, "1", key);
        System.err.println(encryptHex);
        System.err.println(encryptHex.length());
        String decryptContent = Aes256GcmUtils.decryptBase64Text(encryptHex, null, "1", key);
        System.err.println(decryptContent);

    }
}
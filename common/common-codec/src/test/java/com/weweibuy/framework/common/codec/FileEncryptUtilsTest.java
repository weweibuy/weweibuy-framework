package com.weweibuy.framework.common.codec;

import com.weweibuy.framework.common.codec.aes.AESUtils;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class FileEncryptUtilsTest {

    private static final String KEY = "123456789";


    @Test
    public void aesEncrypt() throws Exception {
        SecretKey secretKey = AESUtils.createKey(KEY);
        File file = new File("C:\\Users\\z\\Desktop\\tmp\\test.json");
        File file2 = new File("C:\\Users\\z\\Desktop\\tmp\\test.json.cipher");
        FileEncryptUtils.encrypt(secretKey, AESUtils.ALGORITHM, file, file2);
    }

    @Test
    public void aesEncrypt1() throws Exception {
    }

    @Test
    public void aesDecrypt() throws Exception {
        SecretKey secretKey = AESUtils.createKey(KEY);
        File file = new File("C:\\Users\\z\\Desktop\\tmp\\test.json.cipher");
        File file2 = new File("C:\\Users\\z\\Desktop\\tmp\\test-de.json");
        FileEncryptUtils.decrypt(secretKey, AESUtils.ALGORITHM, file, file2);
    }

    @Test
    public void decrypt() throws Exception {
        SecretKey secretKey = AESUtils.createKey(KEY);
        File file = new File("C:\\Users\\z\\Desktop\\tmp\\test.json.cipher");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.close();
        FileEncryptUtils.decrypt(secretKey, AESUtils.ALGORITHM, file, byteArrayOutputStream);
        String str = byteArrayOutputStream.toString();
        System.err.println(str);
    }

}
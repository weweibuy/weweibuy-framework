package com.weweibuy.framework.common.codec;

import com.weweibuy.framework.common.codec.aes.AESUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AESUtilsTest {

    @Test
    public void writeKey() throws Exception {
        SecretKey secretKey = AESUtils.generateKey();
        SecretKey key = AESUtils.createKey("123456");
        AESUtils.writeKey(key, new File("E:\\tmp\\key\\aes\\aes.key"));
    }

    static String pw = "fc53445b6a2e2b2437f369d7ffbf7d05";

    String src1 = "13800000000";
    String src2 = "154875623012456987";

    @Test
    public void encrypt() throws Exception {
        SecretKey key = AESUtils.createKey(pw);
        String encrypt = AESUtils.encryptToHex(key, src1);
        String decrypt = AESUtils.decryptHex(key, encrypt);
        Assert.assertTrue(decrypt.equals(src1));
        System.err.println(encrypt.length());

        String encrypt2 = AESUtils.encryptToHex(key, src2);
        String decrypt2 = AESUtils.decryptHex(key, encrypt2);
        Assert.assertTrue(decrypt2.equals(src2));
        System.err.println(encrypt2.length());

    }

    @Test
    public void encryptConcurrent() throws Exception {
        SecretKey key = AESUtils.createKey(pw);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        int i = 1000000;
        while (i > 0) {
            executorService.execute(() -> {
                try {
                    en(key);
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                }
            });
            i--;
        }
        Thread.sleep(5000);
        System.err.println(i);
    }


    public void en(SecretKey key) throws InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
        String stringId = "111";
        String encrypt = AESUtils.encryptToHex(key, stringId);
        String decrypt = AESUtils.decryptHex(key, encrypt);
        Assert.assertTrue(stringId.equals(decrypt));
    }


    @Test
    public void generateKey() throws Exception {
        byte[] encoded = AESUtils.generateKey().getEncoded();
        System.err.println(HexUtils.toHexString(encoded));
    }


}
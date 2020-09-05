package com.weweibuy.framework.common.core.utils;

import org.junit.Assert;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AESUtilsTest {

    static String pw = "fc53445b6a2e2b2437f369d7ffbf7d05";

    String src1 = "13800000000";
    String src2 = "154875623012456987";

    @Test
    public void encrypt() throws Exception {
        SecretKey key = AESUtils.createKey(pw);
        String encrypt = AESUtils.encrypt(key, src1);
        String decrypt = AESUtils.decrypt(key, encrypt);
        Assert.assertTrue(decrypt.equals(src1));
        System.err.println(encrypt.length());


        String encrypt2 = AESUtils.encrypt(key, src2);
        String decrypt2 = AESUtils.decrypt(key, encrypt2);
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
        String stringId = IdWorker.nextStringId();
        String encrypt = AESUtils.encrypt(key, stringId);
        String decrypt = AESUtils.decrypt(key, encrypt);
        Assert.assertTrue(stringId.equals(decrypt));
    }


    @Test
    public void generateKey() throws Exception {
        byte[] encoded = AESUtils.generateKey().getEncoded();
        System.err.println(HexUtils.toHexString(encoded));
    }


}
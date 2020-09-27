package com.weweibuy.framework.common.codec;

import com.weweibuy.framework.common.codec.rsa.RSAUtils;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAUtilsTest {


    @Test
    public void generateKey() throws Exception {
        RSAUtils.generateKey("E:/tmp/key/rsa/public.key",
                "E:/tmp/key/rsa/private.key");
    }

    @Test
    public void generateKeyHex() throws Exception {
        RSAUtils.generateKeyBase64("E:/tmp/key/rsa/public64.key",
                "E:/tmp/key/rsa/private64.key");
    }

    @Test
    public void encryptTest() throws Exception {
        byte[] data = "hello cryptography".getBytes();
        PublicKey publicKey = RSAUtils.getPublicKey("E:/tmp/key/rsa/public.key");
        byte[] encrypted = RSAUtils.encrypt(publicKey, data);
        String hexString = HexUtils.toHexString(encrypted);

        PrivateKey privateKey = RSAUtils.getPrivateKey("E:/tmp/key/rsa/private.key");

        byte[] decrypted = RSAUtils.decrypt(privateKey, HexUtils.fromHexString(hexString));

        System.out.println("original: " + new String(data));
        System.out.println("encrypted: " + hexString);
        System.out.println("decrypted: " + new String(decrypted));
    }

    @Test
    public void encrypt64Test() throws Exception {
        String dataStr = "hello cryptography";
        PublicKey publicKey = RSAUtils.getBase64PublicKey("E:/tmp/key/rsa/public64.key");
        PrivateKey privateKey = RSAUtils.getBase64PrivateKey("E:/tmp/key/rsa/private64.key");

        String encrypt = RSAUtils.encrypt(publicKey, dataStr);
        String decryptToStr = RSAUtils.decryptToStr(privateKey, encrypt);

        System.out.println("original: " + dataStr);
        System.out.println("encrypted: " + encrypt);
        System.out.println("decrypted: " + decryptToStr);
    }

    @Test
    public void signTest() throws Exception {
        String dataStr = "hello cryptography";
        PublicKey publicKey = RSAUtils.getBase64PublicKey("E:/tmp/key/rsa/public64.key");
        PrivateKey privateKey = RSAUtils.getBase64PrivateKey("E:/tmp/key/rsa/private64.key");
        String sign = RSAUtils.sign(privateKey, dataStr);
        System.err.println(sign);
        boolean verifySign = RSAUtils.verifySign(publicKey, dataStr, sign);
        System.err.println(verifySign);
    }


}
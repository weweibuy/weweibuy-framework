package com.weweibuy.framework.common.codec;

import com.weweibuy.framework.common.codec.rsa.RSAUtils;
import com.weweibuy.framework.common.codec.rsa.RsaKeyHelper;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class RSAUtilsTest {


    @Test
    public void generateBinaryKey() throws Exception {
        RsaKeyHelper.generateBinaryKeyToFile("tmp/key/rsa/public.key",
                "tmp/key/rsa/private.key");
    }

    @Test
    public void generateBase64Key() throws Exception {
        RsaKeyHelper.generateBase64KeyToFile("tmp/key/rsa/public64.key",
                "tmp/key/rsa/private64.key");
    }

    @Test
    public void encryptTest() throws Exception {
        byte[] data = "hello cryptography".getBytes();
        PublicKey publicKey = RsaKeyHelper.getPublicKeyFromBinaryFile("tmp/key/rsa/public.key");
        String hexString  = RSAUtils.encryptToHex(publicKey, data);

        PrivateKey privateKey = RsaKeyHelper.getPrivateKeyFromBinaryFile("tmp/key/rsa/private.key");
        String decrypted = RSAUtils.decryptHex(privateKey, hexString);


        System.out.println("original: " + new String(data));
        System.out.println("encrypted: " + hexString);
        System.out.println("decrypted: " + decrypted);
    }

    @Test
    public void encrypt64Test() throws Exception {
        String dataStr = "hello cryptography";
        PublicKey publicKey = RsaKeyHelper.getPublicKeyFromBase64File("tmp/key/rsa/public64.key");
        PrivateKey privateKey = RsaKeyHelper.getPrivateKeyFromBase64File("tmp/key/rsa/private64.key");

        String encrypt = RSAUtils.encryptToHex(publicKey, dataStr);
        String decryptToStr = RSAUtils.decryptHex(privateKey, encrypt);

        System.out.println("original: " + dataStr);
        System.out.println("encrypted: " + encrypt);
        System.out.println("decrypted: " + decryptToStr);
    }

    @Test
    public void signTest() throws Exception {
        String dataStr = "hello cryptography";
        PublicKey publicKey = RsaKeyHelper.getPublicKeyFromBase64File("tmp/key/rsa/public64.key");
        PrivateKey privateKey = RsaKeyHelper.getPrivateKeyFromBase64File("tmp/key/rsa/private64.key");
        String sign = RSAUtils.signToHex(privateKey, dataStr, RSAUtils.SIGN_ALGORITHM_SHA256_WITH_RSA);
        System.err.println(sign);
        boolean verifySign = RSAUtils.verifyHexSign(publicKey, dataStr, sign, RSAUtils.SIGN_ALGORITHM_SHA256_WITH_RSA);
        System.err.println(verifySign);
    }


    @Test
    public void test03() throws Exception {
        URL resource = RSAUtilsTest.class.getClassLoader().getResource("key/socialnetwork.cer");
        String path = resource.getPath();
        System.err.println(path);
        X509Certificate x509Certificate = RsaKeyHelper.CertificateHelper.certificateFromFile(path);
        BigInteger serialNumber = x509Certificate.getSerialNumber();
        System.err.println(serialNumber);

        URL resource2 = RSAUtilsTest.class.getClassLoader().getResource("key/socialnetwork.pfx");
        String path2 = resource2.getPath();

        PrivateKey privateKey = RsaKeyHelper.CertificateHelper.privateKeyFromFile(path2, "");
        byte[] encoded = privateKey.getEncoded();
        System.err.println(new String(encoded));
    }

    @Test
    public void test04() throws Exception {
        URL resource = RSAUtilsTest.class.getClassLoader().getResource("key/socialnetwork.cer");
        String path = resource.getPath();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = null;
        StringBuilder keyBuffer = new StringBuilder();
        while ((line = br.readLine()) != null) {
            if (!line.startsWith("-")) {
                keyBuffer.append(line);
            }
        }
        String str = keyBuffer.toString();
        System.err.println(str);
        X509Certificate x509Certificate = RsaKeyHelper.CertificateHelper.certificateFromBase64Text(str);
        System.err.println(x509Certificate.getSerialNumber());
    }

}
package com.weweibuy.framework.common.codec.rsa;

import com.weweibuy.framework.common.codec.HexUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * A simple utility class for easily encrypting and decrypting data using the RSA algorithm.
 *
 * @author Chad Adams
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RSAUtils {

    /**
     * The constant that denotes the algorithm being used.
     */
    private static final String ALGORITHM = "RSA";

    public static final String SIGN_ALGORITHM_SHA256_WITH_RSA = "SHA256withRSA";

    public static final String SIGN_ALGORITHM_SHA1_WITH_RSA = "SHA1withRSA";


    /**
     * 生成一份二进制的秘钥
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void generateBinaryKey(String publicKeyOutput, String privateKeyOutput) throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(2048);

        final KeyPair key = keyGen.generateKeyPair();
        checkAndMkdir(publicKeyOutput, privateKeyOutput);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(publicKeyOutput)))) {
            dos.write(key.getPublic().getEncoded());
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(privateKeyOutput)))) {
            dos.write(key.getPrivate().getEncoded());
        }
    }


    /**
     * 创建文件夹
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     */
    private static void checkAndMkdir(String publicKeyOutput, String privateKeyOutput) {
        File publicKeyFile = new File(publicKeyOutput);
        File publicKeyParentFile = publicKeyFile.getParentFile();
        if (publicKeyParentFile != null && !publicKeyParentFile.exists()) {
            publicKeyParentFile.mkdirs();
        }

        File privateKeyFile = new File(privateKeyOutput);

        File privateKeyParentFile = publicKeyFile.getParentFile();
        if (privateKeyParentFile != null && !privateKeyParentFile.exists()) {
            privateKeyParentFile.mkdirs();
        }
    }

    /**
     * 生成base64的秘钥
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void generateBase64Key(String publicKeyOutput, String privateKeyOutput) throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(2048);
        final KeyPair key = keyGen.generateKeyPair();
        checkAndMkdir(publicKeyOutput, privateKeyOutput);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(publicKeyOutput)))) {
            dos.write(Base64.getEncoder().encode(key.getPublic().getEncoded()));
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(privateKeyOutput)))) {
            dos.write(Base64.getEncoder().encode(key.getPrivate().getEncoded()));
        }
    }

    /**
     * 生成 base64 key
     *
     * @return 数组0:  公钥;  数组1: 私钥
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String[] generateKeyToBase64Str() throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(2048);
        final KeyPair key = keyGen.generateKeyPair();
        byte[] publicKey = Base64.getEncoder().encode(key.getPublic().getEncoded());
        byte[] privateKey = Base64.getEncoder().encode(key.getPrivate().getEncoded());
        return new String[]{new String(publicKey), new String(privateKey)};
    }


    /**
     * The method that will encrypt an array of bytes.
     *
     * @param key  The public key used to encrypt the data.
     * @param data The data in the form of bytes.
     * @return The encrypted bytes, otherwise {@code null} if encryption could not be performed.
     */
    public static byte[] encrypt(PublicKey key, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 加密成16进制数据
     *
     * @param key
     * @param data
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encrypt(PublicKey key, String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return HexUtils.toHexString(cipher.doFinal(data.getBytes()));
    }


    /**
     * The method that will decrypt an array of bytes.
     *
     * @param key           The {@link PrivateKey} used to decrypt the data.
     * @param encryptedData The encrypted byte array.
     * @return The decrypted data, otherwise {@code null} if decryption could not be performed.
     */
    public static byte[] decrypt(PrivateKey key, byte[] encryptedData) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }

    /**
     * 解密
     *
     * @param key
     * @param encryptedHex
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decrypt(PrivateKey key, String encryptedHex) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(HexUtils.fromHexString(encryptedHex));
    }

    /**
     * 解密成 string
     *
     * @param key
     * @param encryptedHex
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decryptToStr(PrivateKey key, String encryptedHex) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return new String(decrypt(key, encryptedHex));
    }

    /**
     * 获取公钥(二进制公钥)
     *
     * @param publicKeyPath The path of the public key file.
     * @return The {@link PublicKey} object.
     * @throws Exception If there was an issue reading the file.
     */
    public static PublicKey getPublicKeyFromBinary(String publicKeyPath) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(Files.readAllBytes(Paths.get(publicKeyPath))));
    }


    /**
     * 获取base64 后的key
     *
     * @param key
     * @return
     */
    public static String getKeyBase64(Key key) {
        byte[] encode = Base64.getEncoder().encode(key.getEncoded());
        return new String(encode);
    }


    /**
     * 获取base64 公钥(公钥为 一行base64的文本, 没有换行)
     *
     * @param publicKeyPath
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromBase64(String publicKeyPath) throws Exception {
        byte[] allBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        byte[] decode = Base64.getDecoder().decode(allBytes);
        return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(decode));
    }

    public static PublicKey getPublicKeyFromBase64Str(String publicKeyBase64Str) throws Exception {
        byte[] decode = Base64.getDecoder().decode(publicKeyBase64Str);
        return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(decode));
    }

    public static PublicKey getPublicKeyFromBase64(byte[] decode) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(decode));
    }

    /**
     * 获取 私钥(二进制)
     *
     * @param privateKeyPath The path of the private key file.
     * @return The {@link PrivateKey} object.
     * @throws Exception If there was an issue reading the file.
     */
    public static PrivateKey getPrivateKeyFromBinary(String privateKeyPath) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(privateKeyPath))));
    }

    /**
     * 获取 base64 私钥
     *
     * @param privateKeyPath
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromBase64(String privateKeyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Files.readAllBytes(Paths.get(privateKeyPath));
        byte[] decode = Base64.getDecoder().decode(bytes);
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decode));
    }

    /**
     * The method that will re-create a {@link PublicKey} from a public key byte array.
     *
     * @param encryptedPublicKey The byte array of a public key.
     * @return The {@link PublicKey} object.
     * @throws Exception If there was an issue reading the byte array.
     */
    public static PublicKey getPublicKey(byte[] encryptedPublicKey) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(encryptedPublicKey));
    }

    /**
     * The method that will re-create a {@link PrivateKey} from a private key byte array.
     *
     * @param encryptedPrivateKey The array of bytes of a private key.
     * @return The {@link PrivateKey} object.
     * @throws Exception If there was an issue reading the byte array.
     */
    public static PrivateKey getPrivateKey(byte[] encryptedPrivateKey) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(encryptedPrivateKey));
    }

    /**
     * 签名
     *
     * @param privateKey
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String sign(PrivateKey privateKey, String data, String signAlgorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return HexUtils.toHexString(signature.sign());
    }


    /**
     * 验签
     *
     * @param publicKey
     * @param data
     * @param sign
     * @return
     */
    public static boolean verifySign(PublicKey publicKey, String data, String sign, String signAlgorithm) {
        try {
            Signature signature = Signature.getInstance(signAlgorithm);
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            return signature.verify(HexUtils.fromHexString(sign));
        } catch (Exception e) {
            return false;
        }
    }

}

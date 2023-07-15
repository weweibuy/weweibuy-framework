package com.weweibuy.framework.common.codec.rsa;

import com.weweibuy.framework.common.codec.HexUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.security.*;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

/**
 * RSA 工具
 * <p>
 * RSA 密钥格式
 * 1. PKCS1
 * 格式:
 * -----BEGIN RSA PRIVATE KEY-----
 * .... (base64内容, 可以提取RSA公钥 + 私钥)
 * -----END RSA PRIVATE KEY-----
 * 常见生成方式:
 * 1.1 ssh-keygen  生成的 id_rsa
 * 1.2 openssl genrsa -out rsa_private_key.pem 2048
 * <p>
 * 2. PKCS8 填充  自己生成的密钥, base64格式,  .pem文件
 * 格式:
 * -----BEGIN PUBLIC KEY-----
 * .... (base64内容, 中可以提取RSA公钥)
 * -----END PUBLIC KEY-----
 * -----BEGIN PRIVATE KEY-----
 * .... (base64内容, 中可以提取RSA私钥)
 * -----END PRIVATE KEY-----
 * <p>
 * 2.1 常见生成方式:  对PKCS1的秘钥导出私钥
 * openssl pkcs8 -topk8 -inform PEM -in rsa_private_key.pem  -outform PEM -out rsa_private_key.p8.pem  -nocrypt
 * openssl rsa -in rsa_private_key_pkcs.pem -pubout -out rsa_public_key.pem
 * 2.2 java  KeyPairGenerator 可以直接生成 被PKCS8填充的内容
 * <p>
 * 3. PKCS12  证书私钥文件, 可以带密码
 * 常见: .pfx 文件
 * <p>
 * 4. cer 证书公钥文件  常见: .cer文件
 * 格式:
 * -----BEGIN CERTIFICATE-----
 * .... (base64内容, 中可以提取证书私钥)
 * -----END CERTIFICATE-----
 *
 * @author Chad Adams
 * 参考: https://github.com/cadamsdev/crypto-utils
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RSAUtils {

    /**
     * The constant that denotes the algorithm being used.
     */
    public static final String ALGORITHM = "RSA";

    public static final String DEFAULT_ALGORITHM_WITH_MODE_PADDING = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    public static final String SIGN_ALGORITHM_SHA256_WITH_RSA = "SHA256withRSA";

    public static final String SIGN_ALGORITHM_SHA1_WITH_RSA = "SHA1withRSA";


    /**
     * The method that will encrypt an array of bytes.
     *
     * @param key  The public key used to encrypt the data.
     * @param data The data in the form of bytes.
     * @return The encrypted bytes, otherwise {@code null} if encryption could not be performed.
     */
    public static byte[] encrypt(PublicKey key, byte[] data) throws InvalidKeyException {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(DEFAULT_ALGORITHM_WITH_MODE_PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
        OAEPParameterSpec parameterSpec = new OAEPParameterSpec("SHA-256", "MGF1",
                new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
        try {
            return cipher.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException("RSA加密参数错误", e);
        }
    }

    /**
     * 加密后转 16进制
     *
     * @param key
     * @param data
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String encryptToHex(PublicKey key, String data) throws InvalidKeyException {
        return encryptToHex(key, data.getBytes());
    }

    /**
     * 加密后转 16进制
     *
     * @param key
     * @param data
     * @return
     * @throws InvalidKeyException
     */
    public static String encryptToHex(PublicKey key, byte[] data) throws InvalidKeyException {
        return HexUtils.toHexString(encrypt(key, data));
    }

    /**
     * 加密后转 base64
     *
     * @param key
     * @param data
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String encryptToBase64(PublicKey key, String data) throws InvalidKeyException {
        return encryptToBase64(key, data.getBytes());
    }

    /**
     * 加密后转 base64
     *
     * @param key
     * @param data
     * @return
     * @throws InvalidKeyException
     */
    public static String encryptToBase64(PublicKey key, byte[] data) throws InvalidKeyException {
        byte[] encode = Base64.getEncoder().encode(encrypt(key, data));
        return new String(encode);
    }


    /**
     * 加密
     *
     * @param key           The {@link PrivateKey} used to decrypt the data.
     * @param encryptedData The encrypted byte array.
     * @return The decrypted data, otherwise {@code null} if decryption could not be performed.
     */
    public static byte[] decrypt(PrivateKey key, byte[] encryptedData) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(DEFAULT_ALGORITHM_WITH_MODE_PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
        OAEPParameterSpec parameterSpec = new OAEPParameterSpec("SHA-256", "MGF1",
                new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
        return cipher.doFinal(encryptedData);
    }

    /**
     * 解密  16进制数据
     *
     * @param key
     * @param encryptedHex
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String decryptHex(PrivateKey key, String encryptedHex) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] decrypt = decrypt(key, HexUtils.fromHexString(encryptedHex));
        return new String(decrypt);
    }

    /**
     * 解密 base64内容
     *
     * @param key
     * @param encryptedBase64
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decryptBase64(PrivateKey key, String encryptedBase64) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return new String(decrypt(key, Base64.getDecoder().decode(encryptedBase64)));
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
    public static String signToHex(PrivateKey privateKey, String data, String signAlgorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return HexUtils.toHexString(signToByte(privateKey, data, signAlgorithm));
    }

    public static String signToHexSha256WithRsa(PrivateKey privateKey, String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return HexUtils.toHexString(signToByte(privateKey, data, SIGN_ALGORITHM_SHA256_WITH_RSA));
    }

    public static byte[] signToByte(PrivateKey privateKey, String data, String signAlgorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return sign(privateKey, data.getBytes(), signAlgorithm);
    }

    public static byte[] sign(PrivateKey privateKey, byte[] data, String signAlgorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public static String signToBase64(PrivateKey privateKey, String data, String signAlgorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] bytes = signToByte(privateKey, data, signAlgorithm);
        return new String(Base64.getEncoder().encode(bytes));
    }

    public static String signToBase64Sha256WithRsa(PrivateKey privateKey, String data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] bytes = signToByte(privateKey, data, SIGN_ALGORITHM_SHA256_WITH_RSA);
        return new String(Base64.getEncoder().encode(bytes));
    }

    /**
     * 验签
     *
     * @param publicKey
     * @param data
     * @param sign          hex签名
     * @param signAlgorithm 算法
     * @return
     */
    public static boolean verifyHexSign(PublicKey publicKey, String data, String sign, String signAlgorithm) {
        return verifySign(publicKey, data.getBytes(), HexUtils.fromHexString(sign), signAlgorithm);
    }

    public static boolean verifyHexSignSha256WithRsa(PublicKey publicKey, String data, String sign, String signAlgorithm) {
        return verifySign(publicKey, data.getBytes(), HexUtils.fromHexString(sign), SIGN_ALGORITHM_SHA256_WITH_RSA);
    }

    public static boolean verifyBase64Sign(PublicKey publicKey, String data, String sign, String signAlgorithm) {
        return verifySign(publicKey, data.getBytes(), Base64.getDecoder().decode(sign), signAlgorithm);
    }

    public static boolean verifyBase64SignSha256WithRsa(PublicKey publicKey, String data, String sign, String signAlgorithm) {
        return verifySign(publicKey, data.getBytes(), Base64.getDecoder().decode(sign), SIGN_ALGORITHM_SHA256_WITH_RSA);
    }

    public static boolean verifySign(PublicKey publicKey, byte[] data, byte[] sign, String signAlgorithm) {
        try {
            Signature signature = Signature.getInstance(signAlgorithm);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            return false;
        }
    }


}

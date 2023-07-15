package com.weweibuy.framework.common.codec.aes;

import com.weweibuy.framework.common.codec.HexUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

/**
 * 注意:  JDK  1.8.0_161 之前AES 只支持 126位即16长度的秘钥
 * 之后可以支持  256位即32长度的秘钥
 *
 * @author durenhao
 * @date 2021/10/30 13:51
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Aes256GcmUtils {

    public static final String ALGORITHM = "AES";

    /**
     * 默认的加密模式和填充算法
     */
    public static final String DEFAULT_ALGORITHM_WITH_MODE_PADDING = "AES/GCM/NoPadding";

    static final int KEY_LENGTH_BYTE = 32;
    static final int TAG_LENGTH_BIT = 128;


    /**
     * 获取 SecretKey
     *
     * @param keyBytes
     * @return
     */
    private static SecretKey secretKey(byte[] keyBytes) {
        if (keyBytes.length != KEY_LENGTH_BYTE) {
            throw new IllegalArgumentException("密钥长度必须为32个字节");
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * 从二进制的 AES的key获取SecretKey
     *
     * @param hexKey64
     * @return
     */
    public static SecretKey secretKeyFromHexKey(String hexKey64) {
        return secretKey(HexUtils.fromHexString(hexKey64));
    }

    /**
     * 从字符中获取 SecretKey
     *
     * @param key32 32长度
     * @return
     */
    public static SecretKey secretKey(String key32) {
        return secretKey(key32.getBytes());
    }

    /**
     * 生成AES KEY
     *
     * @return
     */
    public static SecretKey generateKey() {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    /**
     * 生成 16进制key
     *
     * @return 长度64的 String
     */
    public static String generateKeyHex() {
        return HexUtils.toHexString(generateKey().getEncoded());
    }


    /**
     * 加密
     *
     * @param content
     * @param associatedData
     * @param nonce
     * @param secretKey
     * @return
     * @throws GeneralSecurityException
     */
    public static byte[] encryptToByte(String content, String associatedData, String nonce, SecretKey secretKey)
            throws GeneralSecurityException {
        byte[] associatedByte = null;
        if (associatedData != null) {
            associatedByte = associatedData.getBytes();
        }
        if (nonce == null || nonce.isEmpty()) {
            throw new InvalidParameterException(DEFAULT_ALGORITHM_WITH_MODE_PADDING + " nonce必须有值!");
        }
        return encryptToByte(content.getBytes(), associatedByte, nonce.getBytes(), secretKey);
    }

    /**
     * 加密成16进制数据
     *
     * @param content
     * @param associatedData
     * @param nonce
     * @param secretKey
     * @return
     * @throws GeneralSecurityException
     */
    public static String encryptToHex(String content, String associatedData, String nonce, SecretKey secretKey)
            throws GeneralSecurityException {
        byte[] bytes = encryptToByte(content, associatedData, nonce, secretKey);
        return HexUtils.toHexString(bytes);
    }

    /**
     * 加密成 base64
     *
     * @param content
     * @param associatedData
     * @param nonce
     * @param secretKey
     * @return
     * @throws GeneralSecurityException
     */
    public static String encryptToBase64(String content, String associatedData, String nonce, SecretKey secretKey)
            throws GeneralSecurityException {
        byte[] bytes = encryptToByte(content, associatedData, nonce, secretKey);
        byte[] encode = Base64.getEncoder().encode(bytes);
        return new String(encode);
    }


    /**
     * 解密
     *
     * @param encryptText    加密内容
     * @param associatedData 加密附加数据
     * @param nonce          加密向量
     * @param secretKey      秘钥
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static byte[] decryptToByte(byte[] encryptText, byte[] associatedData, byte[] nonce, SecretKey secretKey)
            throws GeneralSecurityException {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_WITH_MODE_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
            if (associatedData != null) {
                cipher.updateAAD(associatedData);
            }
            return cipher.doFinal(encryptText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException("AES解密失败", e);
        }
    }


    /**
     * 加密
     *
     * @param text           待加密内容
     * @param associatedData 加密附加数据
     * @param nonce          加密向量
     * @param secretKey      秘钥
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static byte[] encryptToByte(byte[] text, byte[] associatedData, byte[] nonce, SecretKey secretKey)
            throws GeneralSecurityException {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_ALGORITHM_WITH_MODE_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
            if (associatedData != null) {
                cipher.updateAAD(associatedData);
            }
            return cipher.doFinal(text);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException("AES加密失败", e);
        }
    }

    /**
     * 解密
     *
     * @param content
     * @param associatedData
     * @param nonce
     * @param secretKey
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String decrypt(byte[] content, String associatedData, String nonce, SecretKey secretKey)
            throws GeneralSecurityException {
        byte[] associatedByte = null;
        if (associatedData != null) {
            associatedByte = associatedData.getBytes();
        }
        if (nonce == null || nonce.isEmpty()) {
            throw new InvalidParameterException(DEFAULT_ALGORITHM_WITH_MODE_PADDING + " nonce必须有值!");
        }
        byte[] bytes = decryptToByte(content, associatedByte, nonce.getBytes(), secretKey);
        return new String(bytes);
    }


    /**
     * 解密 base64内容
     *
     * @param encryptText    被加密后转为base64的内容
     * @param associatedData
     * @param nonce
     * @param secretKey
     * @return
     * @throws GeneralSecurityException
     */
    public static String decryptBase64Text(String encryptText, String associatedData, String nonce, SecretKey secretKey)
            throws GeneralSecurityException {
        return decrypt(Base64.getDecoder().decode(encryptText), associatedData, nonce, secretKey);
    }

    /**
     * 解密 16进制内容
     *
     * @param encryptText    被加密后转为16进制
     * @param associatedData
     * @param nonce
     * @param secretKey
     * @return
     * @throws GeneralSecurityException
     */
    public static String decryptHexText(String encryptText, String associatedData, String nonce, SecretKey secretKey)
            throws GeneralSecurityException {
        return decrypt(HexUtils.fromHexString(encryptText), associatedData, nonce, secretKey);
    }

}

package com.weweibuy.framework.common.core.utils;

import com.weweibuy.framework.common.core.exception.Exceptions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * A simple utility class for easily encrypting and decrypting data using the AES algorithm.
 *
 * @author Chad Adams
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AESUtils {

    private static final String ALGORITHM = "AES";


    public static SecretKey generateKey() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw Exceptions.unknown();
        }
        keyGenerator.init(128);
        return keyGenerator.generateKey();
    }

    /**
     * 生成 16进制key
     *
     * @return
     */
    public static String generateKeyHex() {
        return HexUtils.toHexString(generateKey().getEncoded());
    }


    /**
     * Creates a new {@link SecretKey} based on a password.
     *
     * @param password The password that will be the {@link SecretKey}.
     * @return The key.
     */
    public static SecretKey createKey(String password) {
        byte[] key = password.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw Exceptions.unknown();
        }
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        return new SecretKeySpec(key, ALGORITHM);
    }

    /**
     * Creates a new {@link SecretKey} based on a password with a specified salt.
     *
     * @param salt     The random salt.
     * @param password The password that will be the {@link SecretKey}.
     * @return The key.
     */
    public static SecretKey createKey(byte[] salt, String password) {
        try {
            byte[] key = (salt + password).getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit

            return new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw Exceptions.unknown();
        }

    }

    /**
     * The method that writes the {@link SecretKey} to a file.
     *
     * @param key  The key to write.
     * @param file The file to create.
     * @throws IOException If the file could not be created.
     */
    public static void writeKey(SecretKey key, File file) throws IOException {
        try (FileOutputStream fis = new FileOutputStream(file)) {
            fis.write(key.getEncoded());
        }
    }

    /**
     * Gets a {@link SecretKey} from a {@link File}.
     *
     * @param file The file that is encoded as a key.
     * @return The key.
     * @throws IOException The exception thrown if the file could not be read as a {@link SecretKey}.
     */
    public static SecretKey getSecretKey(File file) throws IOException {
        return new SecretKeySpec(Files.readAllBytes(file.toPath()), ALGORITHM);
    }

    /**
     * The method that will encrypt data.
     *
     * @param secretKey The key used to encrypt the data.
     * @param data      The data to encrypt.
     * @return The encrypted data.
     */
    public static byte[] encrypt(SecretKey secretKey, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密 并转为16进制文本
     *
     * @param secretKey 秘钥
     * @param src
     * @return
     */
    public static String encrypt(SecretKey secretKey, String src) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return HexUtils.toHexString(encrypt(secretKey, src.getBytes()));
    }

    /**
     * The method that will decrypt a piece of encrypted data.
     *
     * @param password  The password used to decrypt the data.
     * @param encrypted The encrypted data.
     * @return The decrypted data.
     */
    public static byte[] decrypt(String password, byte[] encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, AESUtils.createKey(password));
        return cipher.doFinal(encrypted);
    }


    /**
     * The method that will decrypt a piece of encrypted data.
     *
     * @param secretKey The key used to decrypt encrypted data.
     * @param encrypted The encrypted data.
     * @return The decrypted data.
     */
    public static byte[] decrypt(SecretKey secretKey, byte[] encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(encrypted);
    }

    /**
     * 解密
     *
     * @param secretKey 秘钥
     * @param encrypt   16进制密文
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decrypt(SecretKey secretKey, String encrypt) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(HexUtils.fromHexString(encrypt)));
    }


    public static void main(String[] args) throws Exception {

        // the bytes you want to encrypt
        byte[] message = "Hello world!".getBytes();

        // create a key by using your own password
        SecretKey secretKey = AESUtils.createKey("my-password");

        // encrypt the message using the key that was generated
        byte[] encrypted = AESUtils.encrypt(secretKey, message);

        String hexString = HexUtils.toHexString(encrypted);

        byte[] fromHexString = HexUtils.fromHexString(hexString);

        // decrypt the message by entering a password
        byte[] decrypted = AESUtils.decrypt(secretKey, fromHexString);

        // results
        System.out.println("original: " + new String(message));
        System.out.println("encrypted: " + hexString);
        System.out.println("decrypted: " + new String(decrypted));

    }

}

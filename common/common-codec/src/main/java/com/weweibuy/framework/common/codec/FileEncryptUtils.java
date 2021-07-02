package com.weweibuy.framework.common.codec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * 文件加密工具
 *
 * @author durenhao
 * @date 2021/6/27 9:44
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileEncryptUtils {


    public static void encrypt(Key secretKey, String algorithm, File file, File destFile) throws GeneralSecurityException, IOException {
        try (OutputStream out = new FileOutputStream(destFile);
             FileInputStream inputStream = new FileInputStream(file)) {
            encrypt(secretKey, algorithm, inputStream, out);
        }
    }


    public static void encrypt(Key secretKey, String algorithm, InputStream inputStream, File destFile) throws GeneralSecurityException, IOException {
        try (OutputStream out = new FileOutputStream(destFile)) {
            encrypt(secretKey, algorithm, inputStream, out);
        }
    }

    /**
     * @param secretKey    秘钥
     * @param algorithm    加密方式: eg: AES
     * @param inputStream  输入流
     * @param outputStream 加密后输出流
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static void encrypt(Key secretKey, String algorithm, InputStream inputStream, OutputStream outputStream) throws GeneralSecurityException, IOException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        try (CipherInputStream cis = new CipherInputStream(inputStream, cipher)) {
            IOUtils.copy(cis, outputStream);
        }
    }


    public static void decrypt(SecretKey secretKey, String algorithm, File file, File destFile) throws GeneralSecurityException, IOException {
        try (InputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = new FileOutputStream(destFile)) {
            decrypt(secretKey, algorithm, inputStream, outputStream);
        }
    }

    /**
     * 解密
     *
     * @param secretKey    秘钥
     * @param algorithm    加密方式 eg: AES
     * @param inputStream  密文输入流
     * @param outputStream 解密后的输出流
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static void decrypt(SecretKey secretKey, String algorithm, InputStream inputStream, OutputStream outputStream) throws GeneralSecurityException, IOException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        try (CipherOutputStream cos = new CipherOutputStream(outputStream, cipher)) {
            IOUtils.copy(inputStream, cos);
        }
    }

    public static void decrypt(SecretKey secretKey, String algorithm, File encryptFile, OutputStream outputStream) throws GeneralSecurityException, IOException {
        try (InputStream inputStream = new FileInputStream(encryptFile)) {
            decrypt(secretKey, algorithm, inputStream, outputStream);
        }
    }

}

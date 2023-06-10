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
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Enumeration;

/**
 * RSA 工具
 * <p>
 * RSA 密钥填充方式
 * 1. P8 填充  自己生成的密钥, base64格式,  .pem文件
 * 2. P12  证书, 带密码   .cer/.pfx 文件
 *
 * @author Chad Adams
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RSAUtils {

    /**
     * The constant that denotes the algorithm being used.
     */
    private static final String ALGORITHM = "RSA";

    public static final String DEFAULT_ALGORITHM_WITH_MODE_PADDING = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    public static final String SIGN_ALGORITHM_SHA256_WITH_RSA = "SHA256withRSA";

    public static final String SIGN_ALGORITHM_SHA1_WITH_RSA = "SHA1withRSA";


    public static void generateBinaryKey(String publicKeyOutput, String privateKeyOutput) throws NoSuchAlgorithmException, IOException {
        generateBinaryKey(publicKeyOutput, privateKeyOutput, 2048);
    }

    /**
     * 生成一份二进制的秘钥
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void generateBinaryKey(String publicKeyOutput, String privateKeyOutput, Integer keySize) throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(keySize);

        final KeyPair key = keyGen.generateKeyPair();

        try (DataOutputStream dos = new DataOutputStream(FileUtils.openOutputStream(new File(publicKeyOutput), false))) {
            dos.write(key.getPublic().getEncoded());
        }
        try (DataOutputStream dos = new DataOutputStream(FileUtils.openOutputStream(new File(privateKeyOutput), false))) {
            dos.write(key.getPrivate().getEncoded());
        }
    }

    public static void generateBase64Key(String publicKeyOutput, String privateKeyOutput) throws NoSuchAlgorithmException, IOException {
        generateBase64Key(publicKeyOutput, privateKeyOutput, 2048);
    }

    /**
     * 生成base64的秘钥
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void generateBase64Key(String publicKeyOutput, String privateKeyOutput, Integer keySize) throws NoSuchAlgorithmException, IOException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(keySize);
        final KeyPair key = keyGen.generateKeyPair();

        try (DataOutputStream dos = new DataOutputStream(FileUtils.openOutputStream(new File(publicKeyOutput), false))) {
            dos.write(Base64.getEncoder().encode(key.getPublic().getEncoded()));
        }
        try (DataOutputStream dos = new DataOutputStream(FileUtils.openOutputStream(new File(privateKeyOutput), false))) {
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
    public static String[] generateKeyToBase64Str(Integer keySize) throws NoSuchAlgorithmException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
        keyGen.initialize(keySize);
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
        return HexUtils.toHexString(encrypt(key, data.getBytes()));
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
        byte[] encode = Base64.getEncoder().encode(encrypt(key, data.getBytes()));
        return new String(encode);
    }


    /**
     * The method that will decrypt an array of bytes.
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
     * 解密base64
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
        return getPublicKeyFromBase64(decode);
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
    public static PrivateKey getPrivateKeyFromBinaryP8(String privateKeyPath) throws Exception {
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(Paths.get(privateKeyPath))));
    }

    /**
     * 获取 base64 私钥
     *
     * @param privateKeyPath
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromBase64P8(String privateKeyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Files.readAllBytes(Paths.get(privateKeyPath));
        byte[] decode = Base64.getDecoder().decode(bytes);
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decode));
    }

    public static PrivateKey getPrivateKeyFromBase64StrP8(String base64Str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decode = Base64.getDecoder().decode(base64Str.getBytes());
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
    public static PrivateKey getPrivateKeyFromP8(byte[] encryptedPrivateKey) throws Exception {
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

    /**
     * 证书
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CertificateUtils {

        public static final String CERTIFICATE_TYPE = "X509";

        /**
         * 根据 cer文件流, 读取证书
         *
         * @param certificateFileStream
         * @return
         * @throws Exception
         */
        public static X509Certificate certificateFromStream(InputStream certificateFileStream) throws Exception {
            String certificateText = IOUtils.toString(certificateFileStream, StandardCharsets.UTF_8);
            return RsaKeyHelper.parseCertificate(certificateText);
        }


        /**
         * 根据 cer文件流, 读取公钥
         *
         * @param certificateFileStream
         * @return
         * @throws Exception
         */
        public static PublicKey pubKeyFromCerStream(InputStream certificateFileStream) throws Exception {
            return certificateFromStream(certificateFileStream).getPublicKey();
        }

        /**
         * 根据 cer 文件获取证书
         *
         * @param filePath
         * @return
         * @throws Exception
         */
        public static X509Certificate certificateFromFile(String filePath) throws Exception {
            try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
                return certificateFromStream(fileInputStream);
            }
        }

        /**
         * 根据 cer 文件获取公钥
         *
         * @param filePath
         * @return
         * @throws Exception
         */
        public static PublicKey pubKeyFromCerFile(String filePath) throws Exception {
            return certificateFromFile(filePath).getPublicKey();
        }


        /**
         * 根据公钥cer文本串读取证书,
         *
         * @param certificateBase64Text (不包含换行符)
         * @return
         */
        public static X509Certificate certificateFromBase64Text(String certificateBase64Text) throws Exception {
            return certificateFromByte(Base64.getDecoder().decode(certificateBase64Text.getBytes()));
        }


        /**
         * 根据公钥cer文本串读取公钥,
         *
         * @param certificateBase64Text (不包含换行符)
         * @return
         */
        public static PublicKey pubKeyFromCerBase64Text(String certificateBase64Text) throws Exception {
            return certificateFromBase64Text(certificateBase64Text).getPublicKey();
        }

        public static X509Certificate certificateFromByte(byte[] certificateData) throws CertificateException {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE);
            return (X509Certificate) certificateFactory.generateCertificate(
                    new ByteArrayInputStream(certificateData));
        }

        /**
         * 根据私钥文件流读取私钥
         *
         * @param priKeyStream
         * @param priKeyPass   私钥密码
         * @return
         */
        public static PrivateKey privateKeyFromStream(InputStream priKeyStream, String priKeyPass) throws Exception {
            byte[] reads = IOUtils.toByteArray(priKeyStream);
            return privateKeyByStream(reads, priKeyPass);
        }

        /**
         * 根据私钥文件读取私钥
         *
         * @param privateKeyPath
         * @param priKeyPass     私钥密码
         * @return
         * @throws Exception
         */
        public static PrivateKey privateKeyFromFile(String privateKeyPath, String priKeyPass) throws Exception {
            try (FileInputStream fileInputStream = new FileInputStream(privateKeyPath)) {
                return privateKeyFromStream(fileInputStream, priKeyPass);
            }
        }

        /**
         * 根据PFX私钥字节流读取私钥
         *
         * @param pfxBytes
         * @param priKeyPass 私钥密码
         * @return
         */
        public static PrivateKey privateKeyByStream(byte[] pfxBytes, String priKeyPass) throws Exception {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            if (priKeyPass == null) {
                priKeyPass = "";
            }
            char[] charPriKeyPass = priKeyPass.toCharArray();
            ks.load(new ByteArrayInputStream(pfxBytes), charPriKeyPass);

            Enumeration<String> aliasEnum = ks.aliases();
            String keyAlias = null;
            if (aliasEnum.hasMoreElements()) {
                keyAlias = aliasEnum.nextElement();
            }
            return (PrivateKey) ks.getKey(keyAlias, charPriKeyPass);
        }

    }


}

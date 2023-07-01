/*
 * Copyright 2006-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.weweibuy.framework.common.codec.rsa;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1Sequence;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Reads RSA key pairs using BC provider classes but without the
 * need to specify a crypto provider or have BC added as one.
 *
 * @author Luke Taylor
 */
public class RsaKeyHelper {

    private static Charset UTF8 = StandardCharsets.UTF_8;

    private static String BEGIN = "-----BEGIN";

    private static Pattern PEM_DATA = Pattern.compile("-----BEGIN (.*)-----(.*)-----END (.*)-----", Pattern.DOTALL);

    private static final Pattern SSH_PUB_KEY = Pattern.compile("ssh-(rsa|dsa) ([A-Za-z0-9/+]+=*) (.*)");


    /**
     * 生成二进制秘钥
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void generateBinaryKeyToFile(String publicKeyOutput, String privateKeyOutput) throws NoSuchAlgorithmException, IOException {
        generateBinaryKeyToFile(publicKeyOutput, privateKeyOutput, 2048);
    }

    /**
     * 生成秘钥对
     *
     * @param keySize
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeyPair(Integer keySize) throws NoSuchAlgorithmException {
        final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSAUtils.ALGORITHM);
        keyGen.initialize(keySize);
        return keyGen.generateKeyPair();
    }

    /**
     * 生成一份二进制的秘钥
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void generateBinaryKeyToFile(String publicKeyOutput, String privateKeyOutput, Integer keySize) throws NoSuchAlgorithmException, IOException {
        KeyPair key = generateKeyPair(keySize);

        try (DataOutputStream dos = new DataOutputStream(FileUtils.openOutputStream(new File(publicKeyOutput), false))) {
            dos.write(key.getPublic().getEncoded());
        }
        try (DataOutputStream dos = new DataOutputStream(FileUtils.openOutputStream(new File(privateKeyOutput), false))) {
            dos.write(key.getPrivate().getEncoded());
        }
    }

    /**
     * 生成 base64 编码的秘钥
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void generateBase64KeyToFile(String publicKeyOutput, String privateKeyOutput) throws NoSuchAlgorithmException, IOException {
        generateBase64KeyToFile(publicKeyOutput, privateKeyOutput, 2048);
    }

    /**
     * 生成base64的秘钥
     *
     * @param publicKeyOutput
     * @param privateKeyOutput
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static void generateBase64KeyToFile(String publicKeyOutput, String privateKeyOutput, Integer keySize) throws NoSuchAlgorithmException, IOException {
        KeyPair key = generateKeyPair(keySize);

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
        KeyPair key = generateKeyPair(keySize);

        byte[] publicKey = Base64.getEncoder().encode(key.getPublic().getEncoded());
        byte[] privateKey = Base64.getEncoder().encode(key.getPrivate().getEncoded());
        return new String[]{new String(publicKey), new String(privateKey)};
    }


    /**
     * 从 pem秘钥文件流中, 解析秘钥
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static KeyPair parseKeyPair(InputStream inputStream) throws IOException {
        String pemData = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        return parseKeyPair(pemData);
    }

    /**
     * 从pem秘钥解析秘钥
     *
     * @param filePath
     * @return
     */
    public static KeyPair parseKeyPairFromFile(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            return parseKeyPair(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 秘钥文件字符中解析秘钥
     *
     * @param pemData
     * @return
     */
    public static KeyPair parseKeyPair(String pemData) {
        Matcher m = PEM_DATA.matcher(pemData.trim());

        if (!m.matches()) {
            throw new IllegalArgumentException("String is not PEM encoded data");
        }

        String type = m.group(1);
        final byte[] content = Base64.getMimeDecoder().decode(utf8Encode(m.group(2)));
        PublicKey publicKey;
        PrivateKey privateKey = null;

        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            if (type.equals("RSA PRIVATE KEY")) {
                ASN1Sequence seq = ASN1Sequence.getInstance(content);
                if (seq.size() != 9) {
                    throw new IllegalArgumentException("Invalid RSA Private Key ASN1 sequence.");
                }
                org.bouncycastle.asn1.pkcs.RSAPrivateKey key = org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(seq);
                RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
                RSAPrivateCrtKeySpec privSpec = new RSAPrivateCrtKeySpec(key.getModulus(), key.getPublicExponent(),
                        key.getPrivateExponent(), key.getPrime1(), key.getPrime2(), key.getExponent1(), key.getExponent2(),
                        key.getCoefficient());
                publicKey = fact.generatePublic(pubSpec);
                privateKey = fact.generatePrivate(privSpec);
            } else if (type.equals("PUBLIC KEY")) {
                KeySpec keySpec = new X509EncodedKeySpec(content);
                publicKey = fact.generatePublic(keySpec);
            } else if (type.equals("RSA PUBLIC KEY")) {
                ASN1Sequence seq = ASN1Sequence.getInstance(content);
                org.bouncycastle.asn1.pkcs.RSAPublicKey key = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(seq);
                RSAPublicKeySpec pubSpec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
                publicKey = fact.generatePublic(pubSpec);
            } else {
                throw new IllegalArgumentException(type + " is not a supported format");
            }

            return new KeyPair(publicKey, privateKey);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从 pem 公钥中获取公钥
     *
     * @param pemData
     * @return
     */
    public static PublicKey parsePublicKeyPem(String pemData) throws Exception {
        Matcher m = PEM_DATA.matcher(pemData.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException("String is not PEM encoded data");
        }
        String type = m.group(1);

        if (!type.equals("PUBLIC KEY")) {
            throw new IllegalArgumentException(type + " is not a supported format");
        }
        byte[] content = Base64.getMimeDecoder().decode(utf8Encode(m.group(2)));
        return getPublicKey(content);
    }

    /**
     * 从 .cer 文件中读取证书内容
     *
     * @return
     */
    public static X509Certificate parseCertificate(String cerContent) throws Exception {
        Matcher m = PEM_DATA.matcher(cerContent.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException("String is not PEM encoded data");
        }
        String type = m.group(1);

        if (!type.equals("CERTIFICATE")) {
            throw new IllegalArgumentException(type + " is not a supported format");
        }
        byte[] decode = Base64.getMimeDecoder().decode(utf8Encode(m.group(2)));
        return CertificateHelper.certificateFromByte(decode);
    }

    /**
     * 从 pem Pkcs格式 获取私钥
     *
     * @param pemData
     * @return
     */
    public static PrivateKey parsePrivateKeyPemPkcs(String pemPkcsData) throws Exception {
        Matcher m = PEM_DATA.matcher(pemPkcsData.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException("String is not PEM encoded data");
        }
        String type = m.group(1);

        if (!type.equals("PRIVATE KEY")) {
            throw new IllegalArgumentException(type + " is not a supported format");
        }
        byte[] content = Base64.getMimeDecoder().decode(utf8Encode(m.group(2)));
        return getPrivateKey(content);
    }


    /**
     * 解析 ssh 公钥
     *
     * @param key
     * @return
     */
    public static RSAPublicKey parseSSHPublicKey(String key) {
        Matcher m = SSH_PUB_KEY.matcher(key);

        if (m.matches()) {
            String alg = m.group(1);
            String encKey = m.group(2);
            //String id = m.group(3);

            if (!"rsa".equalsIgnoreCase(alg)) {
                throw new IllegalArgumentException("Only RSA is currently supported, but algorithm was " + alg);
            }

            return parseSSHPublicKey0(encKey);
        } else if (!key.startsWith(BEGIN)) {
            // Assume it's the plain Base64 encoded ssh key without the "ssh-rsa" at the start
            return parseSSHPublicKey0(key);
        }

        KeyPair kp = parseKeyPair(key);

        if (kp.getPublic() == null) {
            throw new IllegalArgumentException("Key data does not contain a public key");
        }

        return (RSAPublicKey) kp.getPublic();
    }

    private static RSAPublicKey parseSSHPublicKey0(String encKey) {
        final byte[] PREFIX = new byte[]{0, 0, 0, 7, 's', 's', 'h', '-', 'r', 's', 'a'};
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.getMimeDecoder().decode(utf8Encode(encKey)));
        byte[] prefix = new byte[11];

        try {
            if (in.read(prefix) != 11 || !Arrays.equals(PREFIX, prefix)) {
                throw new IllegalArgumentException("SSH key prefix not found");
            }

            BigInteger e = new BigInteger(readBigInteger(in));
            BigInteger n = new BigInteger(readBigInteger(in));

            return createPublicKey(n, e);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static RSAPublicKey createPublicKey(BigInteger n, BigInteger e) {
        try {
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(n, e));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static byte[] readBigInteger(ByteArrayInputStream in) throws IOException {
        byte[] b = new byte[4];

        if (in.read(b) != 4) {
            throw new IOException("Expected length data as 4 bytes");
        }

        int l = (b[0] << 24) | (b[1] << 16) | (b[2] << 8) | b[3];

        b = new byte[l];

        if (in.read(b) != l) {
            throw new IOException("Expected " + l + " key bytes");
        }

        return b;
    }

    public static byte[] utf8Encode(CharSequence string) {
        try {
            ByteBuffer bytes = UTF8.newEncoder().encode(CharBuffer.wrap(string));
            byte[] bytesCopy = new byte[bytes.limit()];
            System.arraycopy(bytes.array(), 0, bytesCopy, 0, bytes.limit());
            return bytesCopy;
        } catch (CharacterCodingException e) {
            throw new UncheckedIOException(e);
        }
    }


    /**
     * 获取公钥(二进制公钥)
     *
     * @param publicKeyPath 秘钥文件, 文件中只包含秘钥内容
     * @return The {@link PublicKey} object.
     * @throws Exception If there was an issue reading the file.
     */
    public static PublicKey getPublicKeyFromBinaryFile(String publicKeyPath) throws Exception {
        return getPublicKey(Files.readAllBytes(Paths.get(publicKeyPath)));
    }

    /**
     * 二进制流获取秘钥
     *
     * @param inputStream 只包含秘钥内容
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromBinaryStream(InputStream inputStream) throws Exception {
        byte[] bytes = IOUtils.toByteArray(inputStream);
        return getPublicKey(bytes);
    }


    /**
     * 获取base64 公钥(公钥为 一行base64的文本, 没有换行)
     *
     * @param publicKeyPath 秘钥文件, 文件中只包含秘钥内容
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromBase64File(String publicKeyPath) throws Exception {
        byte[] allBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        byte[] decode = Base64.getDecoder().decode(allBytes);
        return getPublicKey(decode);
    }

    /**
     * 获取base64 公钥(公钥为 一行base64的文本, 没有换行)
     *
     * @param publicKeyBase64Str 秘钥内容
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromBase64Str(String publicKeyBase64Str) throws Exception {
        byte[] decode = Base64.getDecoder().decode(publicKeyBase64Str);
        return getPublicKey(decode);
    }

    /**
     * base64 获取公钥
     *
     * @param inputStream 只包含秘钥内容
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromBase64Stream(InputStream inputStream) throws Exception {
        byte[] bytes = IOUtils.toByteArray(inputStream);
        byte[] decode = Base64.getDecoder().decode(bytes);
        return getPublicKey(decode);
    }

    /**
     * 获取 私钥(二进制)
     *
     * @param privateKeyPath 只包含秘钥内容
     * @return The {@link PrivateKey} object.
     * @throws Exception If there was an issue reading the file.
     */
    public static PrivateKey getPrivateKeyFromBinaryFile(String privateKeyPath) throws Exception {
        return getPrivateKey(Files.readAllBytes(Paths.get(privateKeyPath)));
    }

    /**
     * 获取私钥
     *
     * @param privateKeyPath 只包含秘钥内容
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromBase64File(String privateKeyPath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] bytes = Files.readAllBytes(Paths.get(privateKeyPath));
        byte[] decode = Base64.getDecoder().decode(bytes);
        return getPrivateKey(decode);

    }

    public static PrivateKey getPrivateKeyFromBase64Str(String base64Str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decode = Base64.getDecoder().decode(base64Str.getBytes());
        return getPrivateKey(decode);
    }

    /**
     * The method that will re-create a {@link PublicKey} from a public key byte array.
     *
     * @param encryptedPublicKey The byte array of a public key.
     * @return The {@link PublicKey} object.
     * @throws Exception If there was an issue reading the byte array.
     */
    public static PublicKey getPublicKey(byte[] encryptedPublicKey) throws Exception {
        return KeyFactory.getInstance(RSAUtils.ALGORITHM).generatePublic(new X509EncodedKeySpec(encryptedPublicKey));
    }

    /**
     * The method that will re-create a {@link PrivateKey} from a private key byte array.
     *
     * @param encryptedPrivateKey The array of bytes of a private key.
     * @return The {@link PrivateKey} object.
     * @throws Exception If there was an issue reading the byte array.
     */
    public static PrivateKey getPrivateKey(byte[] encryptedPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(RSAUtils.ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(encryptedPrivateKey));
    }

    /**
     * 证书
     */
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CertificateHelper {

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

package com.weweibuy.framework.common.codec.rsa;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RsaKeyHelperTest {


    @Test
    public void parseKeyPair() throws Exception {
        InputStream resourceAsStream = RsaKeyHelperTest.class.getClassLoader().getResourceAsStream("key/jwt_rsa_private_key.pem");
        String string = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        KeyPair keyPair = RsaKeyHelper.parseKeyPair(string);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
    }

}
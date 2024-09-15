package com.weweibuy.framework.common.codec.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.weweibuy.framework.common.codec.rsa.RsaKeyHelper;
import com.weweibuy.framework.common.codec.rsa.RsaKeyHelperTest;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

/**
 * @author durenhao
 * @date 2023/9/8 21:43
 **/
public class Auth0JwtTest {

    @Test
    public void createTest() throws Exception {
        InputStream resourceAsStream = RsaKeyHelperTest.class.getClassLoader().getResourceAsStream("key/jwt_rsa_private_key.pem");
        String string = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        KeyPair keyPair = RsaKeyHelper.parseKeyPair(string);

        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(),
                (RSAPrivateKey) keyPair.getPrivate());


        String token = JWT.create()
                .withIssuer("auth0")
                .sign(algorithm);
        System.err.println(token);
    }

    public static void main(String[] args) throws Exception {
        validateTest();
    }

    public static void validateTest() throws Exception {
        InputStream resourceAsStream = RsaKeyHelperTest.class.getClassLoader().getResourceAsStream("key/jwt_rsa_private_key.pem");
        String string = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        KeyPair keyPair = RsaKeyHelper.parseKeyPair(string);

        Algorithm algorithm = Algorithm.RSA256(null,
                (RSAPrivateKey) keyPair.getPrivate());

        String token = JWT.create()
                // 签发者
                .withIssuer("https://www.baidu.com")
                // 接受者
                .withAudience("user01")
                .withKeyId("123")

                // jwt id
                .withJWTId(UUID.randomUUID().toString())
                // 秘钥id
//                .withKeyId()
                // 主题
                .withSubject("web-token")
                // 签发时间
                .withIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.of("+8")))
                // 过期时间
                .withExpiresAt(LocalDateTime.now().plusSeconds(2).toInstant(
                        ZoneOffset.of("+8")))
                .withClaim("test", "test_claim")
                .sign(algorithm);

        System.err.println(token);


        algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(),
                null);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("https://www.baidu.com")
                .build();

        String keyId = JWT.decode(token)
                .getKeyId();


        DecodedJWT verify = verifier.verify(token);
        String payload = verify.getPayload();
        Map<String, Claim> claims = verify.getClaims();
        System.err.println(payload);
        System.err.println(claims);
    }


}

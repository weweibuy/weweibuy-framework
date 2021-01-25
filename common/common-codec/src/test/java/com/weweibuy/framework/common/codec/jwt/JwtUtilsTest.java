package com.weweibuy.framework.common.codec.jwt;

import com.weweibuy.framework.common.codec.rsa.RsaKeyHelper;
import com.weweibuy.framework.common.codec.rsa.RsaKeyHelperTest;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtilsTest {


    @Test
    public void encode() throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        InputStream resourceAsStream = RsaKeyHelperTest.class.getClassLoader().getResourceAsStream("key/jwt_rsa_private_key.pem");
        String string = IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        KeyPair keyPair = RsaKeyHelper.parseKeyPair(string);

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("userid", "123");
        claims.put("username", "tom");
        claims.put("usertype", "TEST");

        String test_sub = JwtUtils.encodeRS256(keyPair.getPrivate(), claims, "TEST_SUB", System.currentTimeMillis() + 10000);
        System.err.println(test_sub);

        Jwt parse = Jwts.parser()
                .setSigningKey(keyPair.getPublic())
//                .requireNotBefore(new Date(System.currentTimeMillis() - 1000))
                .parse(test_sub);
        Object body = parse.getBody();
        Header header = parse.getHeader();

        Jwt<Header, Object> decode = JwtUtils.parser(keyPair.getPublic(), test_sub);

        System.err.println(decode);
    }

}
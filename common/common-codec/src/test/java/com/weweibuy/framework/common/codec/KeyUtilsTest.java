package com.weweibuy.framework.common.codec;

import com.weweibuy.framework.common.codec.jwt.JwtUtils;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.util.HashMap;

public class KeyUtilsTest {

    @Test
    public void generalHS256Key() throws Exception {
        SecretKey secretKey = KeyUtils.generalHS256Key("123456");
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "tom");
        String encode = JwtUtils.encodeHS256(secretKey, map);
        System.err.println(encode);

        Jwt<Header, Object> parser = JwtUtils.parser(secretKey, encode);

        Object body = parser.getBody();
        System.err.println(body);
    }

}
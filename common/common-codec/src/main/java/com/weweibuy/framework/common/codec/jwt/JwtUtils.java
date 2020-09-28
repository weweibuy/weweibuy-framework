package com.weweibuy.framework.common.codec.jwt;


import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/9/28 20:47
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {


    public static String encode(PrivateKey privateKey, Map<String, Object> claims, String subject, Long expireAt) {
        JwtBuilder builder = Jwts.builder()
                // 这里其实就是new一个JwtBuilder，设置jwt的body
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .setExpiration(new Date(expireAt));
        return builder.compact();
    }


    public static void decode(PublicKey publicKey, String jwt) {
        Jwt parse = Jwts.parser()
                .setSigningKey(publicKey)
                .parse(jwt);
    }


}

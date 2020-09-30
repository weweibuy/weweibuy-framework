package com.weweibuy.framework.common.codec.jwt;


import io.jsonwebtoken.*;
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


    public static String encode(PrivateKey privateKey, Map<String, Object> claims, Map<String, Object> headers, String subject, Long expireAt) {
        JwtBuilder builder = Jwts.builder().setClaims(claims)
                .signWith(SignatureAlgorithm.RS256, privateKey);
        if (headers != null) {
            builder.setHeader(headers);
        }
        if (subject != null) {
            builder.setSubject(subject);
        }
        if (expireAt != null) {
            builder.setExpiration(new Date(expireAt));
        }
        builder.setHeaderParam(Header.TYPE, Header.JWT_TYPE);
        return builder.compact();
    }


    public static String encode(PrivateKey privateKey, Map<String, Object> claims, String subject, Long expireAt) {
        return encode(privateKey, claims, null, subject, expireAt);
    }

    public static String encode(PrivateKey privateKey, Map<String, Object> claims, Long expireAt) {
        return encode(privateKey, claims, null, null, expireAt);
    }


    /**
     * @param publicKey
     * @param jwt
     * @param <H>
     * @param <T>
     * @return
     * @throws MalformedJwtException    if the specified JWT was incorrectly constructed (and therefore invalid).
     *                                  Invalid
     *                                  JWTs should not be trusted and should be discarded.
     * @throws SignatureException       if a JWS signature was discovered, but could not be verified.  JWTs that fail
     *                                  signature validation should not be trusted and should be discarded.
     * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
     *                                  before the time this method is invoked.
     * @throws IllegalArgumentException if the specified string is {@code null} or empty or only whitespace.
     */
    public static <H extends Header, T> Jwt<H, T> parser(PublicKey publicKey, String jwt) {
        return Jwts.parser()
                .setSigningKey(publicKey)
                .parse(jwt);
    }


}

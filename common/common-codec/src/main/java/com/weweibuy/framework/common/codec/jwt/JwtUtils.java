package com.weweibuy.framework.common.codec.jwt;


import io.jsonwebtoken.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PrivateKey;
import java.util.Date;
import java.util.Map;

/**
 * @author durenhao
 * @date 2020/9/28 20:47
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    public static String encode(SecretKey secretKey, Map<String, Object> claims, Map<String, Object> headers, String subject, Long expireAt) {
        return encode(secretKey, SignatureAlgorithm.HS256, claims, headers, subject, expireAt);
    }

    public static String encode(SecretKey secretKey, Map<String, Object> claims, String subject, Long expireAt) {
        return encode(secretKey, SignatureAlgorithm.HS256, claims, null, subject, expireAt);
    }

    public static String encode(SecretKey secretKey, Map<String, Object> claims, Long expireAt) {
        return encode(secretKey, SignatureAlgorithm.HS256, claims, null, null, expireAt);
    }

    public static String encode(SecretKey secretKey, Map<String, Object> claims) {
        return encode(secretKey, SignatureAlgorithm.HS256, claims, null, null, null);
    }


    public static String encode(Key key, SignatureAlgorithm signatureAlgorithm, Map<String, Object> claims, Map<String, Object> headers, String subject, Long expireAt) {
        JwtBuilder builder = Jwts.builder().setClaims(claims)
                .signWith(signatureAlgorithm, key);
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
        return encode(privateKey, SignatureAlgorithm.RS256, claims, null, subject, expireAt);
    }

    public static String encode(PrivateKey privateKey, Map<String, Object> claims, Long expireAt) {
        return encode(privateKey, SignatureAlgorithm.RS256, claims, null, null, expireAt);
    }

    public static String encode(PrivateKey privateKey, Map<String, Object> claims) {
        return encode(privateKey, SignatureAlgorithm.RS256, claims, null, null, null);
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
    public static <H extends Header, T> Jwt<H, T> parser(Key key, String jwt) {
        return Jwts.parser()
                .setSigningKey(key)
                .parse(jwt);
    }


}

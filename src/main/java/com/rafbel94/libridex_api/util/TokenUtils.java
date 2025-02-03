package com.rafbel94.libridex_api.util;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

public class TokenUtils {

    private static final SecretKey key = Jwts.SIG.HS512.key().build();
    private static final long expirationTime = 1000 * 60 * 60 * 24;

    /**
     * Generates a JWT token with the user's specific login data.
     *
     * @param email the email of the user attempting to log in
     * @return a JWT token
     */
    public static String getJWTToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public static boolean isValidToken(String token) {
        try {
            Claims claims = Jwts.parser().decryptWith(key).build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().after(new Date());
        } catch (io.jsonwebtoken.security.SignatureException | ExpiredJwtException e) {
            return false;
        }
    }
}
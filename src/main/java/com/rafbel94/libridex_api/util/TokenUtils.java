package com.rafbel94.libridex_api.util;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Jwts;

public class TokenUtils {
    /**
     * Handles user login requests by validating the provided email and password.
     *
     * @param email    the email of the user attempting to log in
     * @param password the password of the user attempting to log in
     * 
     * @return a JWT token with the user's specific login data
     */
    public static String getJWTToken(String email) {
        long expirationTime = 1000 * 60 * 60 * 24;
        Key key = Jwts.SIG.HS512.key().build();

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }
}

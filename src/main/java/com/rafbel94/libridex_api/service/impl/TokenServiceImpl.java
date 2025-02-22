package com.rafbel94.libridex_api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rafbel94.libridex_api.service.TokenService;
import com.rafbel94.libridex_api.entity.AuthResponse;
import com.rafbel94.libridex_api.entity.User;
import com.rafbel94.libridex_api.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.secretkey}")
    private String jwtSecretKey;

    private SecretKey key;

    @Autowired
    private UserRepository userRepository;

    /**
     * Initializes the secret key for JWT token generation and validation.
     */
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token the JWT token to be validated
     * @return a ResponseEntity containing an error message if validation fails, or null if validation is successful
     */
    @Override
    public ResponseEntity<AuthResponse> validateToken(String token) {
        List<String> messages = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();

        if (token == null || !token.startsWith("Bearer ")) {
            messages.add("An authentication token is mandatory");
            AuthResponse response = new AuthResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        String jwt = token.replace("Bearer ", "");

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt);

            return null;

        } catch (SignatureException e) {
            messages.add("Invalid token");
            AuthResponse response = new AuthResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            messages.add("Token validation failed");
            AuthResponse response = new AuthResponse(false, messages, data);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Generates a JWT token for the provided email.
     *
     * @param email the email for which the JWT token is to be generated
     * @return the generated JWT token
     */
    @Override
    public String getJWTToken(String email) {
        long expirationTime = 1000 * 60 * 60 * 24;

        return Jwts.builder()
                .subject(email)
                .claim("uuid", UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }

    @Override
    public User getUserFromToken(String token) {
        String jwt = token.replace("Bearer ", "");
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt);

        Claims claims = claimsJws.getPayload();
        String email = claims.getSubject();
        return userRepository.findByEmail(email);
    }

    // Is not necessary to store the returned value into a variable since
    // we are not going to use it in this practice, but we are leaving this here
    // for documentation purposes for future doubts
    //
    // HOW TO ACCESS CLAIMS
    // Jws<Claims> claimsJws = Jwts.parser()
    // .verifyWith(key)
    // .build()
    // .parseSignedClaims(jwt);

    // Claims claims = claimsJws.getPayload();
    // String email = claims.getSubject();

}

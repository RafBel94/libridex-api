package com.rafbel94.libridex_api.service;

import org.springframework.http.ResponseEntity;

public interface TokenService {
    ResponseEntity<?> validateToken(String token);
    String getJWTToken(String email);
}
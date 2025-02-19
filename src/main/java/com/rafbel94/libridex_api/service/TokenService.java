package com.rafbel94.libridex_api.service;

import org.springframework.http.ResponseEntity;
import com.rafbel94.libridex_api.entity.User;

public interface TokenService {
    ResponseEntity<?> validateToken(String token);
    String getJWTToken(String email);
    User getUserFromToken(String token);
}
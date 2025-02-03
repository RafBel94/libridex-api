package com.rafbel94.libridex_api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafbel94.libridex_api.entity.User;
import com.rafbel94.libridex_api.service.impl.AuthenticationService;
import com.rafbel94.libridex_api.service.impl.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    @Qualifier("jwtService")
    private final JwtService jwtService;
    
    @Autowired
    @Qualifier("authenticationService")
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = authenticationService.signup(user);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody User user) {
        User authenticatedUser = authenticationService.authenticate(user);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        List<Object> loginResponse = new ArrayList<>();
        loginResponse.add(authenticatedUser);
        loginResponse.add(jwtToken);
        loginResponse.add(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
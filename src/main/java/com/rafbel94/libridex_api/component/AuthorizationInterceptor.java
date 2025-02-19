package com.rafbel94.libridex_api.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.rafbel94.libridex_api.entity.User;
import com.rafbel94.libridex_api.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component("authorizationInterceptor")
public class AuthorizationInterceptor implements HandlerInterceptor{

    @Autowired
    @Qualifier("tokenService")
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        
        var validationResponse = tokenService.validateToken(token);

        if (validationResponse != null) {
            response.setStatus(validationResponse.getStatusCode().value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(validationResponse.getBody()));
            return false;
        }

        User user = tokenService.getUserFromToken(token);
        request.setAttribute("user", user);

        return true;
    }
}

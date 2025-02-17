package com.rafbel94.libridex_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rafbel94.libridex_api.component.AuthorizationInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
    
    @Autowired
    @Qualifier("authorizationInterceptor")
    AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login", "/api/auth/register");
    }
}

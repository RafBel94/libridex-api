package com.rafbel94.libridex_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/api/auth/**").permitAll()
                .requestMatchers("/api/books", "/api/books/**").hasRole("USER")

                // Users allowed accesses
                // Admin only accesses
                // .requestMatchers("/").hasRole("ADMIN")
                // Allow Static resources access
                .requestMatchers(
                        "/resources/**",
                        "/images/**",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/vendor/**",
                        "/fonts/**",
                        "/webjars/**")
                .authenticated()

                // Only allow users to lend, reserve and return.
                // .requestMatchers("/").hasRole("USER")

                .anyRequest().permitAll())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

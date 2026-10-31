package com.ratesrisk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class SecurityConfig {
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/", "/index.html", "/favicon.ico", "/error", "/actuator/health", "/actuator/health/**").permitAll().anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()).build();
    }
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService userDetailsService(@Value("${APP_USER}") String username,
                                                @Value("${APP_PASSWORD}") String password,
                                                PasswordEncoder encoder) {
        if (password.length() < 8) throw new IllegalStateException("APP_PASSWORD must contain at least 8 characters");
        return new InMemoryUserDetailsManager(User.withUsername(username).password(encoder.encode(password)).roles("API").build());
    }
}

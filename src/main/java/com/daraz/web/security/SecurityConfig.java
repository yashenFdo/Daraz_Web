package com.daraz.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public Auth Endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Allow public customer creation (registration) & password resets
                        .requestMatchers(HttpMethod.POST, "/api/v1/customer/create").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/customer/forgot-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/customer/wishlist-reminder/**").permitAll()

                        // Public read endpoints
                        .requestMatchers(HttpMethod.GET, "/api/v1/product/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/product_category/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/review/**").permitAll()

                        // Secure write and user specific endpoints
                        .requestMatchers("/api/v1/cart/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/wishlist/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/review/create").hasAnyRole("USER", "ADMIN")

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }
}

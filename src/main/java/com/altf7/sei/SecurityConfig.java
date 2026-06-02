package com.altf7.sei;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(tokenRepository)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
                                "/api/v1/auth/login/professor",
                                "/api/v1/auth/login/aluno",
                                "/api/v1/jogos",
                                "/api/v1/jogos/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,  "/api/v1/csrf-token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login/professor").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login/aluno").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/jogos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/jogos").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/jogos/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/jogos/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/jogos/{id}").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        return http.build();
    }
}
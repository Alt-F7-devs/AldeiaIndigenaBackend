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
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    private static final String JOGOS_API = "/api/v1/jogos";
    private static final String JOGOS_API_ID = "/api/v1/jogos/{id}";
    private static final String LOGIN_API_PROFESSOR = "/api/v1/auth/login/professor";
    private static final String LOGIN_API_ALUNO = "/api/v1/auth/login/aluno";
    private static final String ADMIN_API = "/api/v1/admin";
    private static final String ALUNO_API = "/api/v1/aluno";
    private static final String ALUNO_API_ID = "/api/v1/aluno/{id_aluno}";
    private static final String PROFESSOR_API = "/api/v1/professor";
    private static final String PROFESSOR_API_ID = "/api/v1/professor/{id_professor}";
    private static final String ADMIN_ALUNO_API = "/api/v1/admin/aluno";
    private static final String ADMIN_ALUNO_API_ID = "/api/v1/admin/aluno/{id_aluno}";
    private static final String ADMIN_PROFESSOR_API = "/api/v1/admin/professor";
    private static final String ADMIN_PROFESSOR_API_ID = "/api/v1/admin/professor/{id_professor}";
    private static final String SALA_API = "/api/v1/sala";
    private static final String SALA_API_ALUNO = "/{id_sala}/aluno/{id_aluno}";
    private static final String SALA_API_ALUNO_LIST = "/api/v1/sala/aluno";
    private static final String SALA_API_ALUNO_LIST_ID = "/api/v1/sala/aluno/{id_aluno}";
    private static final String SALA_API_ID = "/api/v1/sala/{id_sala}";
    private static final String SALA_API_PROFESSOR = "/api/v1/sala/{id_sala}/professor";
    private static final String SALA_API_JOGO = "/api/v1/sala/{id_sala}/jogos/{id_jogo}";
    private static final String SALA_API_JOGO_LIST = "/api/v1/sala/jogos";
    private static final String SALA_API_JOGO_LIST_ID = "/api/v1/sala/jogos/{id}";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                // -- CSRF REATIVADO --
                // Para desabilitar temporariamente: comentar o bloco abaixo e usar a linha .csrf(csrf -> csrf.disable())
                // .csrf(csrf -> csrf.disable())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(tokenRepository)
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
                                LOGIN_API_PROFESSOR,
                                LOGIN_API_ALUNO,
                                JOGOS_API,
                                JOGOS_API_ID,
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                ADMIN_API,
                                ALUNO_API,
                                ALUNO_API_ID
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/csrf-token").permitAll()

                        .requestMatchers(HttpMethod.POST, LOGIN_API_PROFESSOR).permitAll()
                        .requestMatchers(HttpMethod.POST, LOGIN_API_ALUNO).permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, JOGOS_API).permitAll()
                        .requestMatchers(HttpMethod.GET, JOGOS_API).permitAll()
                        .requestMatchers(HttpMethod.PUT, JOGOS_API_ID).permitAll()
                        .requestMatchers(HttpMethod.GET, JOGOS_API_ID).permitAll()
                        .requestMatchers(HttpMethod.DELETE, JOGOS_API_ID).permitAll()
                        .requestMatchers(HttpMethod.POST, ADMIN_API).permitAll()
                        .requestMatchers(HttpMethod.POST, ADMIN_ALUNO_API).permitAll()
                        .requestMatchers(HttpMethod.GET, ADMIN_ALUNO_API).permitAll()
                        .requestMatchers(HttpMethod.GET, ADMIN_ALUNO_API_ID).permitAll()
                        .requestMatchers(HttpMethod.PATCH, ADMIN_ALUNO_API_ID).permitAll()
                        .requestMatchers(HttpMethod.DELETE, ADMIN_ALUNO_API_ID).permitAll()
                        .requestMatchers(HttpMethod.POST, ADMIN_PROFESSOR_API).permitAll()
                        .requestMatchers(HttpMethod.GET, ADMIN_PROFESSOR_API).permitAll()
                        .requestMatchers(HttpMethod.GET, ADMIN_PROFESSOR_API_ID).permitAll()
                        .requestMatchers(HttpMethod.PATCH, ADMIN_PROFESSOR_API_ID).permitAll()
                        .requestMatchers(HttpMethod.DELETE, ADMIN_PROFESSOR_API_ID).permitAll()
                        .requestMatchers(HttpMethod.POST, ALUNO_API).permitAll()
                        .requestMatchers(HttpMethod.GET, ALUNO_API).permitAll()
                        .requestMatchers(HttpMethod.GET, ALUNO_API_ID).permitAll()
                        .requestMatchers(HttpMethod.PATCH, ALUNO_API_ID).permitAll()
                        .requestMatchers(HttpMethod.DELETE, ALUNO_API_ID).permitAll()
                        .requestMatchers(HttpMethod.POST, PROFESSOR_API).permitAll()
                        .requestMatchers(HttpMethod.GET, PROFESSOR_API).permitAll()
                        .requestMatchers(HttpMethod.GET, PROFESSOR_API_ID).permitAll()
                        .requestMatchers(HttpMethod.PATCH, PROFESSOR_API_ID).permitAll()
                        .requestMatchers(HttpMethod.DELETE, PROFESSOR_API_ID).permitAll()
                        .requestMatchers(HttpMethod.POST, SALA_API).permitAll()
                        .requestMatchers(HttpMethod.GET, SALA_API).permitAll()
                        .requestMatchers(HttpMethod.POST, SALA_API_ALUNO).permitAll()
                        .requestMatchers(HttpMethod.GET, SALA_API_ALUNO_LIST).permitAll()
                        .requestMatchers(HttpMethod.GET, SALA_API_ALUNO_LIST_ID).permitAll()
                        .requestMatchers(HttpMethod.GET, SALA_API_ID).permitAll()
                        .requestMatchers(HttpMethod.PATCH, SALA_API_ID).permitAll()
                        .requestMatchers(HttpMethod.DELETE, SALA_API_ID).permitAll()
                        .requestMatchers(HttpMethod.DELETE, SALA_API_PROFESSOR).permitAll()
                        .requestMatchers(HttpMethod.POST, SALA_API_JOGO).permitAll()
                        .requestMatchers(HttpMethod.GET, SALA_API_JOGO_LIST).permitAll()
                        .requestMatchers(HttpMethod.GET, SALA_API_JOGO_LIST_ID).permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                );
        return http.build();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None");
        serializer.setUseSecureCookie(true); // true em produção com HTTPS
        return serializer;
    }
}
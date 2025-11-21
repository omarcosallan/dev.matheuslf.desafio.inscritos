package dev.matheuslf.desafio.inscritos.config;

import dev.matheuslf.desafio.inscritos.security.CustomAccessDeniedHandler;
import dev.matheuslf.desafio.inscritos.security.CustomAuthenticationEntryPoint;
import dev.matheuslf.desafio.inscritos.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String TASKS_PARAMS_PATH = "/tasks/**";
    private static final String PROJECTS_PARAMS_PATH = "/projects/**";

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(PROJECTS_PARAMS_PATH).authenticated()
                        .requestMatchers(HttpMethod.POST, "/tasks").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, TASKS_PARAMS_PATH).authenticated()
                        .requestMatchers(HttpMethod.PUT, TASKS_PARAMS_PATH).authenticated()
                        .requestMatchers(HttpMethod.DELETE, TASKS_PARAMS_PATH).hasRole(ADMIN_ROLE)
                        .anyRequest().authenticated())
                .exceptionHandling(
                        exception ->
                                exception
                                        .accessDeniedHandler(customAccessDeniedHandler)
                                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

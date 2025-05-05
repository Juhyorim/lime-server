package com.lime.server.config.auth;

import com.lime.server.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsService userDetailsService;

    private final String[] swaggerWhiteList = {
            "/swagger-resources/**", "/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/error",
    };

    private final String[] whiteList = {
            "/api/v1/login",
            "/api/v1/join",
            "/api/v1/tico/test"
    };

    //TODO WebSecurityCustomizer로 whiteList 적용 알아보기
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whiteList).permitAll()
                        .requestMatchers(swaggerWhiteList).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Allow preflight requests
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

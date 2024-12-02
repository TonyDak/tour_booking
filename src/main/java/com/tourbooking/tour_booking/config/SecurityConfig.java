package com.tourbooking.tour_booking.config;

import com.tourbooking.tour_booking.controller.AuthenticatonController;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final String[] PUBLIC_ENDPOINTS = {"/v1/auth/login", "/v1/auth/login-google","/v1/booking/chosen-tour", "/v1/booking/payment","/v1/payment/vn-pay-callback","/v1/auth/register", "/v1/auth/introspect", "/v1/auth/logout", "/v1/auth/forgot-password", "/v1/users/forgot-password", "/v1/users/reset-password/**"};
    @Autowired
    private  JwtCustomDecoder jwtCustomDecoder;

    @Autowired
    private AuthenticatonController authenticatonController;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests

                                .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.POST, "v1/tours/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/v1/tours/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/v1/tours/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/v1/tours/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/v1/places/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/v1/locations/**").permitAll()
                                .anyRequest().authenticated()

                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google") // Endpoint bắt đầu login Google
                        .successHandler((request, response, authentication) -> {
                            // Call controller to handle successful login
                            if (authentication != null) {
                                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                                try {
                                    var authResponse = authenticatonController.handleGoogleLogin(token).getResult();
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"authenticated\": " + authResponse.isAuthenticated() + ", \"token\": \"" + authResponse.getToken() + "\"}");
                                    response.getWriter().flush();
                                } catch (MessagingException | IOException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
                            }
                        })
                )
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer
                                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtCustomDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}

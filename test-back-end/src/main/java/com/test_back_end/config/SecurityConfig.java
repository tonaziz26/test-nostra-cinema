package com.test_back_end.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.security.filter.JwtAuthFilter;
import com.test_back_end.security.hendler.FailedHandler;
import com.test_back_end.security.hendler.SuccessHandler;
import com.test_back_end.security.filter.UsernamePasswordAuthFilter;
import com.test_back_end.security.provider.JwtAuthProvider;
import com.test_back_end.security.provider.UsernamePasswordAuthProvider;
import com.test_back_end.security.util.JwtTokenFactory;
import com.test_back_end.security.util.SkipPathRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final static String AUTH_URL = "/v1/login";
    private final static String API = "/api/**";


    private final static List<String> PERMS = List.of(AUTH_URL);
    private final static List<String> AUTH = List.of(API);


    @Autowired
    private UsernamePasswordAuthProvider usernamePasswordAuthProvider;

    @Autowired
    private JwtAuthProvider jwtAuthProvider;

    @Bean
    public FailedHandler usernamePasswordAuthenticationFailureHandler(ObjectMapper objectMapper) {
        return new FailedHandler(objectMapper);
    }

    @Bean
    public SuccessHandler usernamePasswordAuthenticationSuccessHandler(ObjectMapper objectMapper, JwtTokenFactory jwtFactory) {
        return new SuccessHandler(objectMapper, jwtFactory);
    }

    @Autowired
    void registerProvider(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(usernamePasswordAuthProvider)
                .authenticationProvider(jwtAuthProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UsernamePasswordAuthFilter usernamePasswordAuthenticationProcessingFilter(ObjectMapper objectMapper,
                                                                           SuccessHandler successHandler,
                                                                           FailedHandler failedHandler,
                                                                           AuthenticationManager manager) {

        UsernamePasswordAuthFilter filter = new UsernamePasswordAuthFilter(
                AUTH_URL, successHandler, failedHandler, objectMapper);
        filter.setAuthenticationManager(manager);

        return filter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                         UsernamePasswordAuthFilter usernamePasswordAuthProcessingFilter,
                                                         JwtAuthFilter jwtAuthFilter) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(API).authenticated()).csrf(csrf -> csrf.disable())
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(AuthenticationFailureHandler failureHandler, AuthenticationManager authManager) {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(PERMS, AUTH);
        JwtAuthFilter filter = new JwtAuthFilter(matcher, failureHandler);
        filter.setAuthenticationManager(authManager);
        return filter;
    }



}

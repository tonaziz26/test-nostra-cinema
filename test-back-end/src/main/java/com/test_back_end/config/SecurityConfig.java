package com.test_back_end.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.security.filter.EmailAuthenticationFilter;
import com.test_back_end.security.filter.JwtAuthFilter;
import com.test_back_end.security.hendler.EmailSuccessHandler;
import com.test_back_end.security.hendler.FailedHandler;
import com.test_back_end.security.hendler.SuccessHandler;
import com.test_back_end.security.filter.UsernameOtpAuthFilter;
import com.test_back_end.security.provider.EmailAuthProvider;
import com.test_back_end.security.provider.JwtAuthProvider;
import com.test_back_end.security.provider.UsernameOtpAuthProvider;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final static String AUTH_URL_OTP = "/v1/login/otp";
    private final static String AUTH_URL_EMAIL = "/v1/login/email";
    private final static String API = "/api/**";


    private final static List<String> PERMS = List.of(AUTH_URL_OTP, AUTH_URL_EMAIL);
    private final static List<String> AUTH = List.of(API);


    @Autowired
    private UsernameOtpAuthProvider usernameOtpAuthProvider;

    @Autowired
    private EmailAuthProvider emailAuthenticationProvider;


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

    @Bean
    public EmailSuccessHandler emailSuccessHandler(ObjectMapper objectMapper) {
        return new EmailSuccessHandler(objectMapper);
    }

    @Autowired
    void registerProvider(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(emailAuthenticationProvider)
                .authenticationProvider(usernameOtpAuthProvider)
                .authenticationProvider(jwtAuthProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public EmailAuthenticationFilter emailAuthenticationFilter(
            AuthenticationManager authenticationManager,
            EmailSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            ObjectMapper objectMapper) {
        EmailAuthenticationFilter emailAuthenticationFilter = new EmailAuthenticationFilter(AUTH_URL_EMAIL, successHandler, failureHandler,
                objectMapper);
        emailAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return emailAuthenticationFilter;
    }

    @Bean
    public UsernameOtpAuthFilter usernamePasswordAuthenticationProcessingFilter(ObjectMapper objectMapper,
                                                                                SuccessHandler successHandler,
                                                                                FailedHandler failedHandler,
                                                                                AuthenticationManager manager) {

        UsernameOtpAuthFilter filter = new UsernameOtpAuthFilter(
                AUTH_URL_OTP, successHandler, failedHandler, objectMapper);
        filter.setAuthenticationManager(manager);

        return filter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                         UsernameOtpAuthFilter usernamePasswordAuthProcessingFilter,
                                                         JwtAuthFilter jwtAuthFilter) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(AUTH_URL_EMAIL).permitAll()
                .requestMatchers(AUTH_URL_OTP).permitAll()
                .requestMatchers(API).authenticated()
            ).csrf(AbstractHttpConfigurer::disable)
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

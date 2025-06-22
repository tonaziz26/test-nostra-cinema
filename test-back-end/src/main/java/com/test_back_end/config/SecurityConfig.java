package com.test_back_end.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.repository.AccountRepository;
import com.test_back_end.repository.UserLoginRepository;
import com.test_back_end.security.filter.EmailAuthenticationFilter;
import com.test_back_end.security.filter.JwtAuthFilter;
import com.test_back_end.security.hendler.EmailSuccessHandler;
import com.test_back_end.security.hendler.FailedHandler;
import com.test_back_end.security.hendler.FailedOtpHandler;
import com.test_back_end.security.hendler.SuccessHandler;
import com.test_back_end.security.filter.SessionIdOtpAuthFilter;
import com.test_back_end.security.provider.EmailAuthProvider;
import com.test_back_end.security.provider.JwtAuthProvider;
import com.test_back_end.security.provider.SessionIdOtpAuthProvider;
import com.test_back_end.security.util.JwtTokenFactory;
import com.test_back_end.security.util.SkipPathRequestMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final static String AUTH_URL_OTP = "/v1/otp";
    private final static String AUTH_URL = "/v1/login";
    private final static String CITY = "/api/city/**";
    private final static String MOVIE = "/api/movie/**";
    private final static String SESSION = "/api/session/**";
    private final static String SWAGGER_UI = "/swagger-ui/**";
    private final static String SWAGGER_API_DOCS = "/v3/api-docs/**";
    private final static String SWAGGER_CONFIG = "/configuration/**";
    private final static String WEBJARS = "/webjars/**";

    private final static String ACCOUNT = "/api/account/**";
    private final static String LAYOUT_STUDIO = "/api/layout-studio/**";
    private final static String PAYMENT = "/api/payment/**";
    private final static String MOVIE_ADMIN = "/api/movie-admin/**";

    private final static List<String> PERMS = List.of(AUTH_URL_OTP, AUTH_URL, CITY, MOVIE, SESSION,
                                               SWAGGER_UI, SWAGGER_API_DOCS, SWAGGER_CONFIG, WEBJARS);
    private final static List<String> AUTHENTICATION = List.of(ACCOUNT, LAYOUT_STUDIO, PAYMENT, MOVIE_ADMIN);


    @Autowired
    private SessionIdOtpAuthProvider usernameOtpAuthProvider;

    @Autowired
    private EmailAuthProvider emailAuthenticationProvider;


    @Autowired
    private JwtAuthProvider jwtAuthProvider;

    @Bean
    public FailedHandler usernamePasswordAuthenticationFailureHandler(ObjectMapper objectMapper) {
        return new FailedHandler(objectMapper);
    }

    @Bean
    @Qualifier("usernamePasswordAuthenticationFailureHandler")
    public AuthenticationFailureHandler authenticationFailureHandler(FailedHandler failedHandler) {
        return failedHandler;
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
    void registerProvider(AuthenticationManagerBuilder auth) {
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
            @Qualifier("usernamePasswordAuthenticationFailureHandler") AuthenticationFailureHandler failureHandler,
            ObjectMapper objectMapper) {
        EmailAuthenticationFilter emailAuthenticationFilter = new EmailAuthenticationFilter(AUTH_URL, successHandler, failureHandler,
                objectMapper);
        emailAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return emailAuthenticationFilter;
    }

    @Bean
    public SessionIdOtpAuthFilter usernamePasswordAuthenticationProcessingFilter(ObjectMapper objectMapper,
                                                                                 SuccessHandler successHandler,
                                                                                 FailedOtpHandler failedHandler,
                                                                                 UserLoginRepository userLoginRepository,
                                                                                 AccountRepository accountRepository,
                                                                                 AuthenticationManager manager) {

        SessionIdOtpAuthFilter filter = new SessionIdOtpAuthFilter(
                AUTH_URL_OTP, successHandler, failedHandler, objectMapper, userLoginRepository, accountRepository);
        filter.setAuthenticationManager(manager);

        return filter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                         SessionIdOtpAuthFilter usernamePasswordAuthProcessingFilter,
                                                         JwtAuthFilter jwtAuthFilter) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(PERMS.toArray(new String[0])).permitAll()
                .requestMatchers(AUTHENTICATION.toArray(new String[0])).authenticated()
            ).csrf(AbstractHttpConfigurer::disable)
            .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(@Qualifier("usernamePasswordAuthenticationFailureHandler") AuthenticationFailureHandler failureHandler, AuthenticationManager authManager) {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(PERMS, AUTHENTICATION);
        JwtAuthFilter filter = new JwtAuthFilter(matcher, failureHandler);
        filter.setAuthenticationManager(authManager);
        return filter;
    }

    @Bean
    public FailedOtpHandler failedOtpHandler(ObjectMapper objectMapper, AccountRepository accountRepository) {
        return new FailedOtpHandler(objectMapper, accountRepository);
    }
}

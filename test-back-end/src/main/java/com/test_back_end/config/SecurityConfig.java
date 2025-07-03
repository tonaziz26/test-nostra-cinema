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
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final static String AUTH_URL_OTP = "/v1/otp";
    private final static String AUTH_URL = "/v1/login";
    private final static String CITY = "/v1/cities/**";
    private final static String SESSION = "/v1/sessions/**";
    private final static String SWAGGER_UI = "/swagger-ui/**";
    private final static String SWAGGER_API_DOCS = "/v3/api-docs/**";
    private final static String SWAGGER_CONFIG = "/configuration/**";
    private final static String WEBJARS = "/webjars/**";

    private final static String ACCOUNT = "/v1/account/**";
    private final static String LAYOUT_SEAT = "/v1/layout-seats/**";
    private final static String PAYMENT = "/v1/payments/**";

    private final static List<String> PERMS = List.of(AUTH_URL_OTP, AUTH_URL, CITY, SESSION,
                                               SWAGGER_UI, SWAGGER_API_DOCS, SWAGGER_CONFIG, WEBJARS);
    private final static List<String> AUTHENTICATION = List.of(ACCOUNT, LAYOUT_SEAT, PAYMENT);


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
    public SuccessHandler usernamePasswordAuthenticationSuccessHandler(ObjectMapper objectMapper, JwtTokenFactory jwtFactory, AccountRepository accountRepository) {
        return new SuccessHandler(objectMapper, jwtFactory, accountRepository);
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
                        .requestMatchers(HttpMethod.GET, "/v1/movies/presigned-upload").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/movies").authenticated()
                        .requestMatchers(HttpMethod.GET, "/v1/movies/**").permitAll()
                        .requestMatchers(PERMS.toArray(new String[0])).permitAll()
                        .requestMatchers(AUTHENTICATION.toArray(new String[0])).authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(
            @Qualifier("usernamePasswordAuthenticationFailureHandler") AuthenticationFailureHandler failureHandler,
            AuthenticationManager authManager) {

        List<RequestMatcher> skipMatchers = List.of(
                new AntPathRequestMatcher("/v1/otp"),
                new AntPathRequestMatcher("/v1/login"),
                new AntPathRequestMatcher("/v1/cities/**"),
                new AntPathRequestMatcher("/v1/sessions/**"),
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/configuration/**"),
                new AntPathRequestMatcher("/webjars/**"),
                new AntPathRequestMatcher("/v1/movies/**", HttpMethod.GET.name()) // granular
        );

        List<RequestMatcher> authMatchers = List.of(
                new AntPathRequestMatcher("/v1/account/**"),
                new AntPathRequestMatcher("/v1/layout-seats/**"),
                new AntPathRequestMatcher("/v1/payments/**"),
                new AntPathRequestMatcher("/v1/movies", HttpMethod.POST.name()),
                new AntPathRequestMatcher("/v1/movies/presigned-upload", HttpMethod.GET.name())
        );

        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(skipMatchers, authMatchers);
        JwtAuthFilter filter = new JwtAuthFilter(matcher, failureHandler);
        filter.setAuthenticationManager(authManager);
        return filter;
    }


    @Bean
    public FailedOtpHandler failedOtpHandler(ObjectMapper objectMapper, AccountRepository accountRepository) {
        return new FailedOtpHandler(objectMapper, accountRepository);
    }
}

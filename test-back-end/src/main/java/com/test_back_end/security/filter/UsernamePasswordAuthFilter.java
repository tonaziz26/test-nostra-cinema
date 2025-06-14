package com.test_back_end.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.security.LoginRequestDto;
import com.test_back_end.security.hendler.FailedHandler;
import com.test_back_end.security.hendler.SuccessHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class UsernamePasswordAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final SuccessHandler successHandler;
    private final FailedHandler failedHandler;
    private final ObjectMapper objectMapper;

    public UsernamePasswordAuthFilter(String defaultFilterProcessesUrl, SuccessHandler successHandler, FailedHandler failedHandler, ObjectMapper objectMapper) {
        super(defaultFilterProcessesUrl);
        this.successHandler = successHandler;
        this.failedHandler = failedHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginRequestDto loginDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);
        // filter manual validation c: username blank dkk

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
//        ctx.setAuthentication(authResult);  // hasil autentikasi disimpan disini
//        SecurityContextHolder.setContext(ctx); // disimpan di SecurityContext
//        chain.doFilter(request, response);  // trsin ke filter selanjutnya

        this.successHandler.onAuthenticationSuccess(request, response, authResult);

        // this ambil yg custom kalo super yang bawaanya
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.failedHandler.onAuthenticationFailure(request, response, failed);
    }
}

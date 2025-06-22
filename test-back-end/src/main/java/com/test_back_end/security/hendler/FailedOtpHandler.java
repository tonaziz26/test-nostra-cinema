package com.test_back_end.security.hendler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.entity.Account;
import com.test_back_end.repository.AccountRepository;
import com.test_back_end.repository.UserLoginRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FailedOtpHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    private final AccountRepository accountRepository;

    public FailedOtpHandler(ObjectMapper objectMapper, AccountRepository accountRepository) {
        this.objectMapper = objectMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message =(String) request.getAttribute("message");


        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", null != message ? message : "Authentication failed");
        responseMap.put("status_code", HttpStatus.UNAUTHORIZED.value());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), responseMap);
    }
}

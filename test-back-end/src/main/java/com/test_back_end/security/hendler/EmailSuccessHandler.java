package com.test_back_end.security.hendler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.security.model.EmailAuthToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmailSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public EmailSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        EmailAuthToken emailAuthToken = (EmailAuthToken) authentication;
        String sessionId = emailAuthToken.getSessionId();

        Map<String, String> responseMap = new HashMap<>();

        responseMap.put("sessionId", sessionId);
        responseMap.put("response", "check your email for verification code");
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), responseMap);
    }
}
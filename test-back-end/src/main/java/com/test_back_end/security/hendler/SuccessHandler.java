package com.test_back_end.security.hendler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.security.model.AccessJwtToken;
import com.test_back_end.security.util.JwtTokenFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenFactory jwtFactory;

    public SuccessHandler(ObjectMapper objectMapper, JwtTokenFactory jwtFactory) {
        this.objectMapper = objectMapper;
        this.jwtFactory = jwtFactory;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Map<String, String> responseMap = new HashMap<>();


        AccessJwtToken jwtToken = jwtFactory.createAccessJwtToken(userDetails.getUsername(), userDetails.getAuthorities());

        responseMap.put("token", jwtToken.getRawToken());
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getWriter(), responseMap);
    }
}

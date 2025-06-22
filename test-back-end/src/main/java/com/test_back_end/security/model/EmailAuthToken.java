package com.test_back_end.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class EmailAuthToken extends AbstractAuthenticationToken {
    private final String email;
    private String sessionId;

    public EmailAuthToken(String email) {
        super(null);
        this.email = email;
        this.setAuthenticated(false);
    }



    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
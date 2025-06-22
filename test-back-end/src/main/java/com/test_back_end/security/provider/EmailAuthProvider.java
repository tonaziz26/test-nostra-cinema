package com.test_back_end.security.provider;

import com.test_back_end.security.model.EmailAuthToken;
import com.test_back_end.service.AccountService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailAuthProvider implements AuthenticationProvider {

    private final AccountService accountService;

    public EmailAuthProvider(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailAuthToken token = (EmailAuthToken) authentication;
        String email = token.getEmail();
        String sessionId = UUID.randomUUID().toString();

        accountService.generateOTP(email, sessionId);

        EmailAuthToken authenticatedToken = new EmailAuthToken(email);
        authenticatedToken.setSessionId(sessionId);
        authenticatedToken.setAuthenticated(true);

        return authenticatedToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthToken.class.isAssignableFrom(authentication);
    }
}
package com.test_back_end.security.provider;


import com.test_back_end.security.model.EmailAuthToken;
import com.test_back_end.service.AccountService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

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

        try {
            accountService.generateOTP(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return new EmailAuthToken(email);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthToken.class.isAssignableFrom(authentication);
    }
}
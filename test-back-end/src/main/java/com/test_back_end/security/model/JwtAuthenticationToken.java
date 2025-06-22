package com.test_back_end.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private UserDetails userDetails;

    private RawAccessJwtToken rawAccessJwtToken;

    public JwtAuthenticationToken(RawAccessJwtToken rawAccessJwtToken) {
        super(null);
        this.rawAccessJwtToken = rawAccessJwtToken;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(UserDetails userDetails,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(false);
        this.userDetails = userDetails;
        this.setAuthenticated(true);

    }

    @Override
    public Object getCredentials() {
        return this.rawAccessJwtToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }
}

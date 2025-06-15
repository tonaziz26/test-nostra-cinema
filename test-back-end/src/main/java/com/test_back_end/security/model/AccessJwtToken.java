package com.test_back_end.security.model;

import io.jsonwebtoken.Claims;

public class AccessJwtToken implements Token{

    private final String rawToken;

    private Claims claims;

    public AccessJwtToken(String rawToken, Claims claims) {
        super();
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public String getRawToken() {
        return rawToken;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }

    @Override
    public String getToken() {
        return rawToken;
    }
}

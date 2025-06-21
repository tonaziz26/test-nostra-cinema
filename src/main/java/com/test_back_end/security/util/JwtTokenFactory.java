package com.test_back_end.security.util;


import com.test_back_end.security.model.AccessJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtTokenFactory {

    private final Key secret;

    public JwtTokenFactory(Key secret) {
        this.secret = secret;
    }

    public AccessJwtToken createAccessJwtToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().subject(username)
                .add("scopes", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).build();

        LocalDateTime currentTime = LocalDateTime.now();
        Date CurrentTimeDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

        LocalDateTime expiryTime = currentTime.plusDays(1);
        Date expiryTimeDate = Date.from(expiryTime.atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .claims(claims)
                .issuer("https://nostra-cimnema.com")
                .issuedAt(CurrentTimeDate)
                .expiration(expiryTimeDate)
                .signWith(secret)
                .compact();

        return new AccessJwtToken(token, claims);

    }



}
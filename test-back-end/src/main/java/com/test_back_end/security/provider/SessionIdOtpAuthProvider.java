package com.test_back_end.security.provider;

import com.test_back_end.entity.UserLogin;
import com.test_back_end.entity.sql_response.AccountSQL;
import com.test_back_end.repository.UserLoginRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionIdOtpAuthProvider implements AuthenticationProvider {

    private final UserLoginRepository userLoginRepository;

    public SessionIdOtpAuthProvider(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof AccountSQL accountSQL)) {
            throw new IllegalArgumentException("Authentication must be of type AccountSQL");
        }

        UserLogin userLogin = userLoginRepository.findBySessionId(accountSQL.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("User Login not found"));


        userLogin.setUsed(true);
        userLoginRepository.save(userLogin);

        List<String> role = List.of(accountSQL.getRoleName());

        Collection<GrantedAuthority> authorities = role.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = User.builder()
                .username(accountSQL.getSessionId())
                .password("")
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                authorities
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccountSQL.class.isAssignableFrom(authentication);
    }
}

package com.test_back_end.entity.sql_response;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;

public class AccountSQL extends UsernamePasswordAuthenticationToken {

    private Long id;
    private String name;
    private String sessionId;
    private String otp;
    private LocalDateTime expiredTime;
    private String roleName;
    private Boolean used;

    public AccountSQL(Long id, String name, String sessionId, String otp, LocalDateTime expiredTime, String roleName, Boolean used) {
        super(sessionId, otp);
        this.id = id;
        this.name = name;
        this.sessionId = sessionId;
        this.otp = otp;
        this.expiredTime = expiredTime;
        this.roleName = roleName;
        this.used = used;
        this.setAuthenticated(false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @Override
    public Object getCredentials() {
        return otp;
    }

    @Override
    public Object getPrincipal() {
        return sessionId;
    }
}

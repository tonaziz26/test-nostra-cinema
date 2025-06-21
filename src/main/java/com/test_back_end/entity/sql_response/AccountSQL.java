package com.test_back_end.entity.sql_response;


import java.time.LocalDateTime;

public class AccountSQL {

    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime expiredTime;
    private String roleName;

    public AccountSQL(Long id, String name, String email, String password, LocalDateTime expiredTime, String roleName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.expiredTime = expiredTime;
        this.roleName = roleName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}

package com.test_back_end.dto;

public class AccountResponseDTO {
    private String secureId;
    private String name;
    private String email;
    
    public AccountResponseDTO() {
    }
    
    public AccountResponseDTO(String secureId, String name, String email) {
        this.secureId = secureId;
        this.name = name;
        this.email = email;
    }

    public String getSecureId() {
        return secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
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
}

package com.test_back_end.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransactionRequestDTO {
    
    @NotNull(message = "Account ID is required")
    private String accountId;
    
    @NotNull(message = "Studio session ID is required")
    private Long studioSessionId;
    
    @NotBlank(message = "Chair number is required")
    private String chairNumber;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Long getStudioSessionId() {
        return studioSessionId;
    }

    public void setStudioSessionId(Long studioSessionId) {
        this.studioSessionId = studioSessionId;
    }

    public String getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(String chairNumber) {
        this.chairNumber = chairNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

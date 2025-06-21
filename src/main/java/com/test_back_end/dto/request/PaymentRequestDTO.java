package com.test_back_end.dto.request;

import com.test_back_end.validation.AvailableChair;
import com.test_back_end.validation.LayoutPosition;
import com.test_back_end.validation.TransactionNotBooked;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

@AvailableChair
@TransactionNotBooked
@LayoutPosition
public class PaymentRequestDTO {
    
    private BigDecimal totalPrice;

    @NotNull(message = "session movie id is required")
    private Long sessionMovieId;

    @NotEmpty(message = "Transactions cannot be empty")
    private List<@Valid TransactionRequestDTO> transactions;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<TransactionRequestDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionRequestDTO> transactions) {
        this.transactions = transactions;
    }

    public Long getSessionMovieId() {
        return sessionMovieId;
    }

    public void setSessionMovieId(Long sessionMovieId) {
        this.sessionMovieId = sessionMovieId;
    }
}

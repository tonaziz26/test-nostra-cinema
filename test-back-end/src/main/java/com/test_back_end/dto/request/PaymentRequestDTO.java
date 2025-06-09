package com.test_back_end.dto.request;

import com.test_back_end.validation.FutureOrTodayEpoch;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class PaymentRequestDTO {
    
    @NotNull(message = "Booking date is required")
    @FutureOrTodayEpoch(message = "Booking date must be today or in the future")
    private Long bookingDateEpoch;
    
    private BigDecimal totalPrice;

    @NotEmpty(message = "Transactions cannot be empty")
    private List<@Valid TransactionRequestDTO> transactions;

    public Long getBookingDateEpoch() {
        return bookingDateEpoch;
    }

    public void setBookingDateEpoch(Long bookingDateEpoch) {
        this.bookingDateEpoch = bookingDateEpoch;
    }

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
}

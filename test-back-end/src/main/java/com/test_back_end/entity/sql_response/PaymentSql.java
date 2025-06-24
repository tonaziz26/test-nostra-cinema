package com.test_back_end.entity.sql_response;

import com.test_back_end.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentSql {

    private String secureId;
    private String paymentNumber;
    private PaymentStatus status;
    private LocalDateTime expiredTime;
    private LocalDateTime bookingDate;
    private BigDecimal totalPrice;
    private String location;
    private String studioNumber;
    private String transactionId;
    private String chairNumber;
    private String userName;
    private LocalDateTime paymentDate;

    public PaymentSql(String secureId, String paymentNumber, PaymentStatus status, LocalDateTime expiredTime,
                      LocalDateTime bookingDate, BigDecimal totalPrice, String location, String studioNumber,
                      String transactionId, String chairNumber, String userName, LocalDateTime paymentDate) {
        this.secureId = secureId;
        this.paymentNumber = paymentNumber;
        this.status = status;
        this.expiredTime = expiredTime;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
        this.location = location;
        this.studioNumber = studioNumber;
        this.transactionId = transactionId;
        this.chairNumber = chairNumber;
        this.userName = userName;
        this.paymentDate = paymentDate;
    }

    public String getSecureId() {
        return secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStudioNumber() {
        return studioNumber;
    }

    public void setStudioNumber(String studioNumber) {
        this.studioNumber = studioNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(String chairNumber) {
        this.chairNumber = chairNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}

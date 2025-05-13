package com.test_back_end.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secure_id")
    private String secureId;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @JoinColumn(name = "session_movie_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SessionMovie sessionMovie;


    @Column(name = "chair_number")
    private String chairNumber;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    private String status;

    @JoinColumn(name = "payment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecureId() {
        return secureId;
    }

    public void setSecureId(String secureId) {
        this.secureId = secureId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public SessionMovie getSessionMovie() {
        return sessionMovie;
    }

    public void setSessionMovie(SessionMovie sessionMovie) {
        this.sessionMovie = sessionMovie;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}

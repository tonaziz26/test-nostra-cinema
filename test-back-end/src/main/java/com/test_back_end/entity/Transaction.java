package com.test_back_end.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "secure_id", nullable = false)
    private String secureId;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @JoinColumn(name = "studio_session_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StudioSession studioSession;

    @JoinColumn(name = "session_movie_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SessionMovie sessionMovie;


    @Column(name = "chair_number", nullable = false)
    private String chairNumber;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

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

    public StudioSession getStudioSession() {
        return studioSession;
    }

    public void setStudioSession(StudioSession studioSession) {
        this.studioSession = studioSession;
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


    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public SessionMovie getSessionMovie() {
        return sessionMovie;
    }

    public void setSessionMovie(SessionMovie sessionMovie) {
        this.sessionMovie = sessionMovie;
    }
}

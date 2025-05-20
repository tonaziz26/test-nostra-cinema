package com.test_back_end.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "studio")
public class Studio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @JoinColumn(name = "theater_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Theater theater;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }
}

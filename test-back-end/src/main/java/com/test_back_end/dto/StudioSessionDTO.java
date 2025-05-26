package com.test_back_end.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

public class StudioSessionDTO {

    private Long id;
    private LocalTime startTime;
    private BigDecimal price;

    private String studioNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStudioNumber() {
        return studioNumber;
    }

    public void setStudioNumber(String studioNumber) {
        this.studioNumber = studioNumber;
    }
}

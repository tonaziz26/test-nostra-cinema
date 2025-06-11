package com.test_back_end.entity.sql_response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StudioSessionSQL {

    private Long theaterId;
    private String theaterName;
    private String theaterCode;
    private String theaterAddress;

    private Long studioSessionMovieId;
    private LocalDateTime startTime;
    private BigDecimal price;
    private BigDecimal additionalPrice;

    private String StudioNumber;


    public StudioSessionSQL(Long theaterId, String theaterName, String theaterCode, String theaterAddress,
                            Long studioSessionMovieId, LocalDateTime startTime, BigDecimal price,
                            BigDecimal additionalPrice, String studioNumber) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterCode = theaterCode;
        this.theaterAddress = theaterAddress;
        this.studioSessionMovieId = studioSessionMovieId;
        this.startTime = startTime;
        this.price = price;
        this.additionalPrice = additionalPrice;
        this.StudioNumber = studioNumber;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public String getTheaterCode() {
        return theaterCode;
    }

    public void setTheaterCode(String theaterCode) {
        this.theaterCode = theaterCode;
    }

    public String getTheaterAddress() {
        return theaterAddress;
    }

    public void setTheaterAddress(String theaterAddress) {
        this.theaterAddress = theaterAddress;
    }

    public Long getStudioSessionMovieId() {
        return studioSessionMovieId;
    }

    public void setStudioSessionMovieId(Long studioSessionMovieId) {
        this.studioSessionMovieId = studioSessionMovieId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(BigDecimal additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public String getStudioNumber() {
        return StudioNumber;
    }

    public void setStudioNumber(String studioNumber) {
        StudioNumber = studioNumber;
    }
}

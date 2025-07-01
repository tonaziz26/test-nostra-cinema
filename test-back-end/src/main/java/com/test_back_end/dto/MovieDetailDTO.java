package com.test_back_end.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test_back_end.enums.MovieStatus;

import java.time.LocalDate;

public class MovieDetailDTO {
    private Long id;
    @JsonProperty("title")
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String urlImage;
    private MovieStatus status;

    public MovieDetailDTO() {
    }

    public MovieDetailDTO(Long id, String name, LocalDate startDate, LocalDate endDate, String urlImage, MovieStatus status) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.urlImage = urlImage;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }
}

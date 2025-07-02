package com.test_back_end.dto;

import com.test_back_end.enums.MovieStatus;

import java.time.LocalDate;

public class MovieDetailDTO {
    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private String urlImage;
    private MovieStatus status;

    public MovieDetailDTO() {
    }

    public MovieDetailDTO(Long id, String title, LocalDate startDate, LocalDate endDate, String urlImage, MovieStatus status) {
        this.id = id;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

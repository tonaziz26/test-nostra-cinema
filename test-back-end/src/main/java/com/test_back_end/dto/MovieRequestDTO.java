package com.test_back_end.dto;

import com.test_back_end.validation.FutureOrTodayEpoch;
import com.test_back_end.validation.UniqueMovieName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MovieRequestDTO {

    @NotBlank(message = "Name is required")
    @UniqueMovieName(message = "Movie name already exists")
    private String name;

    @NotNull(message = "Start date is required")
    @FutureOrTodayEpoch
    private Long startDate;

    @NotNull(message = "End date is required")
    @FutureOrTodayEpoch
    private Long endDate;

    private String imageFileName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}

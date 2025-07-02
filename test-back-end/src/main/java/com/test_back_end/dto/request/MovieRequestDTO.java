package com.test_back_end.dto.request;

import com.test_back_end.validation.EndDateAfterStartDate;
import com.test_back_end.validation.FutureOrTodayEpoch;
import com.test_back_end.validation.UniqueMovieName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@EndDateAfterStartDate()
public class MovieRequestDTO {

    @NotBlank(message = "Title is required")
    @UniqueMovieName()
    private String title;

    @NotNull(message = "Start date is required")
    @FutureOrTodayEpoch
    private Long startDate;

    @NotNull(message = "End date is required")
    @FutureOrTodayEpoch
    private Long endDate;

    private String imageFileName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

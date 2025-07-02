package com.test_back_end.controller;

import com.test_back_end.dto.TheaterDto;
import com.test_back_end.service.StudioSessionService;
import com.test_back_end.validation.FutureOrTodayDate;
import com.test_back_end.validation.FutureOrTodayEpoch;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/v1/sessions")
@Validated
public class StudioSessionController {

    private final StudioSessionService studioSessionService;

    @Autowired
    public StudioSessionController(StudioSessionService studioSessionService) {
        this.studioSessionService = studioSessionService;
    }

    @GetMapping("")
    public ResponseEntity<Set<TheaterDto>> getSessionList(
            @RequestParam(name = "city_code") @NotNull(message = "City code is required") String cityCode,
            @RequestParam(name = "movie_id") @NotNull(message = "Movie id is required") Long movieId,
            @RequestParam(name = "date") @NotNull(message = "Date is required") @FutureOrTodayEpoch Long date) {
        
        Set<TheaterDto> theaters = studioSessionService.getSessionList(cityCode, movieId, date);
        return ResponseEntity.ok(theaters);
    }
}

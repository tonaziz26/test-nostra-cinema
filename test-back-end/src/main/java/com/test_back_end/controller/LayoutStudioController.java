package com.test_back_end.controller;

import com.test_back_end.dto.LayoutStudioDTO;
import com.test_back_end.service.LayoutStudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/layout-studio")
public class LayoutStudioController {

    @Autowired
    private LayoutStudioService layoutStudioService;


    @GetMapping("/studio-session")
    public ResponseEntity<List<LayoutStudioDTO>> getLayoutStudio(
            @RequestParam(name = "studio_session_id") Long studioSessionId,
            @RequestParam(name = "booking_date") Long bookingDate) {

        List<LayoutStudioDTO> layoutStudios = layoutStudioService.getLayoutStudio(studioSessionId, bookingDate);

        return ResponseEntity.ok(layoutStudios);
    }
}

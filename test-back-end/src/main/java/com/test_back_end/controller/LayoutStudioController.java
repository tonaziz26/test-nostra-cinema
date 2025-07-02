package com.test_back_end.controller;

import com.test_back_end.dto.LayoutStudioDTO;
import com.test_back_end.service.LayoutStudioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/layout-seats")
@SecurityRequirement(name = "Bearer Authentication")
public class LayoutStudioController {

    @Autowired
    private LayoutStudioService layoutStudioService;


    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<LayoutStudioDTO>> getLayoutStudio(
            @RequestParam(name = "session_movie_id") Long sessionMovieId) {

        List<LayoutStudioDTO> layoutStudios = layoutStudioService.getLayoutStudio(sessionMovieId);

        return ResponseEntity.ok(layoutStudios);
    }
}

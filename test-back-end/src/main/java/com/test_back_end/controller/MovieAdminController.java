package com.test_back_end.controller;

import com.test_back_end.dto.MovieDTO;
import com.test_back_end.dto.PresignedUrlResponseDTO;
import com.test_back_end.dto.request.MovieRequestDTO;
import com.test_back_end.service.MovieService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movie-admin")
@SecurityRequirement(name = "Bearer Authentication")
public class MovieAdminController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/upload-image")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PresignedUrlResponseDTO> getPresignedUrl(@RequestParam("filename") String filename) throws Exception {
        return ResponseEntity.ok(movieService.getPresignedUrl(filename));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MovieDTO> createMovie(@RequestBody @Valid MovieRequestDTO request) {
        return ResponseEntity.ok(movieService.createMovie(request));
    }
}

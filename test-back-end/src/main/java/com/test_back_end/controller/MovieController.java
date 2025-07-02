package com.test_back_end.controller;

import com.test_back_end.dto.MovieDTO;
import com.test_back_end.dto.MovieDetailDTO;
import com.test_back_end.dto.PageResultDTO;
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
@RequestMapping("/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("active")
    public ResponseEntity<PageResultDTO<MovieDTO>> getMovies(
            @RequestParam(name = "city_code") String cityCode,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "title") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        
        return ResponseEntity.ok(movieService.getMovies(cityCode, page, size, sort, direction));
    }

    @GetMapping("")
    public ResponseEntity<PageResultDTO<MovieDTO>> getListMovie(
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "title") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        return ResponseEntity.ok(movieService.getListMovie(title, page, size, sort, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailDTO> getMovieDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(movieService.getMovieDetail(id));
    }

    @GetMapping("/presigned-upload")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PresignedUrlResponseDTO> getPresignedUrl(@RequestParam("filename") String filename) throws Exception {
        return ResponseEntity.ok(movieService.getPresignedUrl(filename));
    }

    @PostMapping("")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MovieDTO> createMovie(@RequestBody @Valid MovieRequestDTO request) {
        return ResponseEntity.ok(movieService.createMovie(request));
    }

}

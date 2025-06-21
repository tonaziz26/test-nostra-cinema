package com.test_back_end.controller;

import com.test_back_end.dto.MovieDTO;
import com.test_back_end.dto.MovieDetailDTO;
import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("now-showing")
    public ResponseEntity<PageResultDTO<MovieDTO>> getMovies(
            @RequestParam(name = "city_code") String cityCode,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        
        return ResponseEntity.ok(movieService.getMovies(cityCode, page, size, sort, direction));
    }

    @GetMapping("")
    public ResponseEntity<PageResultDTO<MovieDTO>> getListMovie(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {

        return ResponseEntity.ok(movieService.getListMovie(name, page, size, sort, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailDTO> getMovieDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(movieService.getMovieDetail(id));
    }


}

package com.test_back_end.controller;

import com.test_back_end.dto.MovieDTO;
import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("now-showing")
    public ResponseEntity<PageResultDTO<MovieDTO>> getMovies(
            @RequestParam(name = "city_id") Long cityId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        
        return ResponseEntity.ok(movieService.getMovies(cityId, page, size, sort, direction));
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


}

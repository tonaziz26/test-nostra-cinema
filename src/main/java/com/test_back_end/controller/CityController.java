package com.test_back_end.controller;

import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.entity.City;
import com.test_back_end.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<PageResultDTO<City>> getAllCities(
            @RequestParam(name = "name", defaultValue = "", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        
        return ResponseEntity.ok(cityService.getCitiesByName(name, page, size, sort, direction));

    }
}

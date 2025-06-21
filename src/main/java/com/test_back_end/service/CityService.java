package com.test_back_end.service;

import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.entity.City;
import com.test_back_end.repository.CityRepository;
import com.test_back_end.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    
    public PageResultDTO<City> getCitiesByName(String name, int page, int limit, String sort, String direction) {

        Sort.Direction dir = PaginationUtil.getSortDirection(direction);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(dir, sort)));

        Page<City> cityPage = cityRepository.findByNameContainingIgnoreCase(name, pageable);

        return new PageResultDTO<>(cityPage.getContent(), cityPage.getTotalPages(), cityPage.getTotalElements());
    }

}

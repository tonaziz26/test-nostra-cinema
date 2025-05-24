package com.test_back_end.service;

import com.test_back_end.dto.MovieDTO;
import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.entity.Movie;
import com.test_back_end.repository.MovieRepository;
import com.test_back_end.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public PageResultDTO<MovieDTO> getMovies(Long cityId, int page, int limit, String sort, String direction) {
        Sort.Direction dir = PaginationUtil.getSortDirection(direction);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(dir, sort)));

        Page<Movie> moviePage = movieRepository.findMoviesByCityIdAndDateRange(cityId, LocalDate.now(), pageable);

        List<MovieDTO> movieDTOs = moviePage.getContent().stream()
                .map(m -> new MovieDTO(
                        m.getId(),
                        m.getName(),
                        m.getStartDate(),
                        m.getEndDate(),
                        m.getUrlImage()
                ))
                .collect(Collectors.toList());
        
        return new PageResultDTO<>(movieDTOs, moviePage.getTotalPages(), moviePage.getTotalElements());
    }
}

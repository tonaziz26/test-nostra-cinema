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

        return new PageResultDTO<>(toMovieDTOs(moviePage), moviePage.getTotalPages(), moviePage.getTotalElements());
    }

    public PageResultDTO<MovieDTO> getListMovie(String name, int page, int limit, String sort, String direction) {
        Sort.Direction dir = PaginationUtil.getSortDirection(direction);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(dir, sort)));

        Page<Movie> moviePage = movieRepository.findByNameContainingIgnoreCase(name, pageable);

        return new PageResultDTO<>(toMovieDTOs(moviePage), moviePage.getTotalPages(), moviePage.getTotalElements());
    }

    private List<MovieDTO> toMovieDTOs(Page<Movie> moviePage) {
        return moviePage.getContent().stream()
                .map(this::toMovieDTO)
                .collect(Collectors.toList());
    }

    private MovieDTO toMovieDTO(Movie movie) {
        return new MovieDTO(
                movie.getId(),
                movie.getName(),
                movie.getUrlImage()
        );
    }
}

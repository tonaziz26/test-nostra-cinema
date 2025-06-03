package com.test_back_end.service;

import com.test_back_end.config.MinioProperties;
import com.test_back_end.dto.MovieRequestDTO;
import com.test_back_end.dto.MovieDTO;
import com.test_back_end.dto.MovieDetailDTO;
import com.test_back_end.dto.PresignedUrlResponseDTO;
import com.test_back_end.enums.MovieStatus;
import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.entity.Movie;
import com.test_back_end.repository.MovieRepository;
import com.test_back_end.util.PaginationUtil;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioProperties minioProperties;

    public PresignedUrlResponseDTO getPresignedUrl(String filename) throws Exception {

        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(minioProperties.getBucketName())
                        .object(filename)
                        .method(Method.PUT)
                        .expiry(10, TimeUnit.MINUTES)
                        .build()
        );

        return new PresignedUrlResponseDTO(url, filename);
    }

    public MovieDTO createMovie(MovieRequestDTO request) {
        Movie movie = new Movie();
        movie.setName(request.getName());

        movie.setStartDate(Instant.ofEpochMilli(request.getStartDate())
                .atZone(ZoneId.systemDefault())
                .toLocalDate());

        movie.setEndDate(Instant.ofEpochMilli(request.getEndDate())
                .atZone(ZoneId.systemDefault())
                .toLocalDate());

        if (request.getImageFileName() != null)
            movie.setUrlImage(minioProperties.getUrl() + "/" + minioProperties.getBucketName() + "/" + request.getImageFileName());


        Movie savedMovie = movieRepository.save(movie);
        return toMovieDTO(savedMovie);
    }

     public PageResultDTO<MovieDTO> getMovies(String  cityCode, int page, int limit, String sort, String direction) {
        Sort.Direction dir = PaginationUtil.getSortDirection(direction);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(dir, sort)));

        Page<Movie> moviePage = movieRepository.findMoviesByCityCodeAndDateRange(cityCode, LocalDate.now(), pageable);

        return new PageResultDTO<>(toMovieDTOs(moviePage), moviePage.getTotalPages(), moviePage.getTotalElements());
    }

    public PageResultDTO<MovieDTO> getListMovie(String name, int page, int limit, String sort, String direction) {
        Sort.Direction dir = PaginationUtil.getSortDirection(direction);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(dir, sort)));

        Page<Movie> moviePage = movieRepository.findByNameContainingIgnoreCase(name, pageable);

        return new PageResultDTO<>(toMovieDTOs(moviePage), moviePage.getTotalPages(), moviePage.getTotalElements());
    }
    
    public MovieDetailDTO getMovieDetail(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));

        LocalDate currentDate = LocalDate.now();
        MovieStatus status;

        if (currentDate.isBefore(movie.getStartDate())) {
            status = MovieStatus.COMING_SOON;
        } else if (currentDate.isAfter(movie.getEndDate())) {
            status = MovieStatus.NOT_AVAILABLE;
        } else {
            status = MovieStatus.NOW_PLAYING;
        }

        return new MovieDetailDTO(
                movie.getId(),
                movie.getName(),
                movie.getStartDate(),
                movie.getEndDate(),
                movie.getUrlImage(),
                status
        );
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

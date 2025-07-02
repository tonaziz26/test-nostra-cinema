package com.test_back_end.repository;

import com.test_back_end.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    @Query("SELECT DISTINCT m FROM Movie m " +
            "INNER JOIN m.studioSessions ss " +
            "INNER JOIN ss.studio s " +
            "INNER JOIN s.theater t " +
            "INNER JOIN t.city c " +
           "WHERE c.code = :cityCode AND :currentDate BETWEEN m.startDate AND m.endDate")
    Page<Movie> findMoviesByCityCodeAndDateRange(
            @Param("cityCode") String cityCode,
            @Param("currentDate") LocalDate currentDate,
            Pageable pageable);

    Page<Movie> findByTitleContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByTitleIgnoreCase(String trim);
}

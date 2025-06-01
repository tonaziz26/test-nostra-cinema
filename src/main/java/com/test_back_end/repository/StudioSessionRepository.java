package com.test_back_end.repository;

import com.test_back_end.entity.StudioSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudioSessionRepository extends JpaRepository<StudioSession, Long> {

    @Query("SELECT DISTINCT ss FROM StudioSession ss " +
            "INNER JOIN SessionMovie sm on sm.studioSession.id = ss.id " +
            "INNER JOIN Movie m on m.id = sm.movie.id " +
            "INNER JOIN Studio s on s.id = ss.studio.id " +
            "INNER JOIN Theater t on t.id = s.theater.id " +
            "INNER JOIN City c on c.id = t.city.id " +
            "WHERE m.id = :movieId " +
            "AND :date BETWEEN m.startDate AND m.endDate " +
            "AND c.id = :cityId")
    List<StudioSession> findSessionByCityIdAndDateRange(
            @Param("cityId") Long cityId,
            @Param("movieId") Long movieId,
            @Param("date") LocalDate date);

}

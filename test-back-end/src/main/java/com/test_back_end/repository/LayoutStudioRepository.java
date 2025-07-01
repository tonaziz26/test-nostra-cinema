package com.test_back_end.repository;

import com.test_back_end.entity.LayoutStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LayoutStudioRepository extends JpaRepository<LayoutStudio, Long> {

    @Query(value = "select a.id, a.chair_number, a.y_layout, a.x_layout,  " +
            "case when t.id is null then a.status else 'BOOKED' end status, " +
            "a.studio_id  " +
            "from ( " +
            "select ls.* from layout_studio ls  " +
            "            inner join studio s on ls.studio_id = s.id  " +
            "            inner join studio_session ss on ss.studio_id = s.id  " +
            "            inner join session_movie sm on ss.id = sm.studio_session_id  " +
            "            where sm.id = :sessionMovieId  " +
            ") a " +
            "left join ( " +
            "SELECT t.* FROM transaction t " +
            "            inner join payment p ON t.payment_id = p.id " +
            "            where p.session_movie_id = :sessionMovieId " +
            "            AND ((p.status = 'WAITING_FOR_PAYMENT' AND p.expired_time > NOW()) OR p.status = 'SUCCESS') " +
            ") t on t.chair_number = a.chair_number ", nativeQuery = true)
    List<LayoutStudio> findBySessionMovieId(Long sessionMovieId);


    @Query(value = """
            SELECT ls.id, ls.chair_number, ls.y_layout, ls.x_layout,
              CASE
                WHEN t.chair_number IS NULL AND ls.chair_number NOT IN (:chairNumbers) THEN ls.status
                ELSE 'BOOKED'
              END AS status,
              ls.studio_id
            FROM layout_studio ls
            LEFT JOIN (
              SELECT t.chair_number
              FROM transaction t
              JOIN payment p ON t.payment_id = p.id
              WHERE p.session_movie_id = :sessionMovieId
                AND ((p.status = 'WAITING_FOR_PAYMENT' AND p.expired_time > NOW()) OR p.status = 'SUCCESS')
            ) t ON t.chair_number = ls.chair_number
            WHERE ls.studio_id = (
              SELECT s.id
              FROM studio s
              JOIN studio_session ss ON ss.studio_id = s.id
              JOIN session_movie sm ON ss.id = sm.studio_session_id
              WHERE sm.id = :sessionMovieId
              LIMIT 1
            )
            AND (
              CASE
                WHEN t.chair_number IS NULL AND ls.chair_number NOT IN (:chairNumbers) THEN ls.status
                ELSE 'BOOKED'
              END
            ) = 'AVAILABLE'
            
            AND EXISTS (
              SELECT 1
              FROM layout_studio ls_left
              LEFT JOIN (
                SELECT t.chair_number
                FROM transaction t
                JOIN payment p ON t.payment_id = p.id
                WHERE p.session_movie_id = :sessionMovieId
                  AND ((p.status = 'WAITING_FOR_PAYMENT' AND p.expired_time > NOW()) OR p.status = 'SUCCESS')
              ) t_left ON t_left.chair_number = ls_left.chair_number
              WHERE ls_left.studio_id = ls.studio_id
                AND ls_left.y_layout = ls.y_layout
                AND ls_left.x_layout = ls.x_layout - 1
                AND (
                  CASE
                    WHEN ls_left.chair_number IN (:chairNumbers) THEN 'BOOKED'
                    WHEN t_left.chair_number IS NULL THEN ls_left.status
                    ELSE 'BOOKED'
                  END
                ) != 'AVAILABLE'
                AND (
                  CASE
                    WHEN ls_left.chair_number IN (:chairNumbers) THEN 'BOOKED'
                    WHEN t_left.chair_number IS NULL THEN ls_left.status
                    ELSE 'BOOKED'
                  END
                ) != 'NOT_AVAILABLE'
            )
            
            AND EXISTS (
              SELECT 1
              FROM layout_studio ls_right
              LEFT JOIN (
                SELECT t.chair_number
                FROM transaction t
                JOIN payment p ON t.payment_id = p.id
                WHERE p.session_movie_id = :sessionMovieId
                  AND ((p.status = 'WAITING_FOR_PAYMENT' AND p.expired_time > NOW()) OR p.status = 'SUCCESS')
              ) t_right ON t_right.chair_number = ls_right.chair_number
              WHERE ls_right.studio_id = ls.studio_id
                AND ls_right.y_layout = ls.y_layout
                AND ls_right.x_layout = ls.x_layout + 1
                AND (
                  CASE
                    WHEN ls_right.chair_number IN (:chairNumbers) THEN 'BOOKED'
                    WHEN t_right.chair_number IS NULL THEN ls_right.status
                    ELSE 'BOOKED'
                  END
                ) != 'AVAILABLE'
                AND (
                  CASE
                    WHEN ls_right.chair_number IN (:chairNumbers) THEN 'BOOKED'
                    WHEN t_right.chair_number IS NULL THEN ls_right.status
                    ELSE 'BOOKED'
                  END
                ) != 'NOT_AVAILABLE'
            )
            """, nativeQuery = true)
    List<LayoutStudio> findBySessionMovieIdAndNewChairNumber(
            @Param("sessionMovieId") Long sessionMovieId,
            @Param("chairNumbers") List<String> chairNumbers
    );


    @Query(value = "select ls.* from layout_studio ls " +
            "inner join studio s on ls.studio_id = s.id " +
            "inner join studio_session ss on ss.studio_id = s.id " +
            "inner join session_movie sm on ss.id = sm.studio_session_id  " +
            "where sm.id = :sessionMovieId " +
            "and UPPER(ls.chair_number) = UPPER(:chairNumber) ", nativeQuery = true)
    Optional<LayoutStudio> findBySessionMovieIdAndChairNumber(Long sessionMovieId, String chairNumber);
}

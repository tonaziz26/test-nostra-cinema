package com.test_back_end.repository;

import com.test_back_end.entity.LayoutStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "select ls.* from layout_studio ls " +
            "inner join studio s on ls.studio_id = s.id " +
            "inner join studio_session ss on ss.studio_id = s.id " +
            "inner join session_movie sm on ss.id = sm.studio_session_id  " +
            "where sm.id = :sessionMovieId " +
            "and UPPER(ls.chair_number) = UPPER(:chairNumber) ", nativeQuery = true)
    Optional<LayoutStudio> findBySessionMovieIdAndChairNumber(Long sessionMovieId, String chairNumber);
}

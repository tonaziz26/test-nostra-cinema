package com.test_back_end.repository;

import com.test_back_end.entity.LayoutStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LayoutStudioRepository extends JpaRepository<LayoutStudio, Long> {

    @Query(value = "select ls.* from layout_studio ls " +
            "inner join studio s on ls.studio_id = s.id " +
            "inner join studio_session ss on ss.studio_id = s.id " +
            "where ss.id = :studioSessionId", nativeQuery = true)
    List<LayoutStudio> findByStudioSessionId(Long studioSessionId);

    @Query(value = "select ls.* from layout_studio ls " +
            "inner join studio s on ls.studio_id = s.id " +
            "inner join studio_session ss on ss.studio_id = s.id " +
            "where ss.id = :studioSessionId " +
            "and UPPER(ls.chair_number) = UPPER(:chairNumber) ", nativeQuery = true)
    Optional<LayoutStudio> findByStudioSessionIdAndChairNumber(Long studioSessionId, String chairNumber);
}

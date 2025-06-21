package com.test_back_end.repository;

import com.test_back_end.entity.SessionMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionMovieRepository extends JpaRepository<SessionMovie, Long> {
}

package com.test_back_end.repository;

import com.test_back_end.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT t.* FROM transaction t " +
            "inner join payment p ON t.payment_id = p.id " +
            "WHERE p.session_movie_id = :sessionMovieId " +
            "AND ((p.status = 'WAITING_FOR_PAYMENT' AND p.expired_time > NOW()) OR p.status = 'SUCCESS') " +
            "AND t.chair_number in :chairNumbers " ,
            nativeQuery = true)
    List<Transaction> findByPaymentBookingDateAndSessionId(
            @Param("sessionMovieId") Long sessionMovieId,
            @Param("chairNumbers") List<String> chairNumbers);

}

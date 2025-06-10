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
            "WHERE p.booking_date = :bookingDate " +
            "AND t.studio_session_id = :studioSessionId",
            nativeQuery = true)
    List<Transaction> findByPaymentBookingDateAndSessionId(
            @Param("bookingDate") LocalDateTime bookingDate,
            @Param("studioSessionId") Long studioSessionId);

}

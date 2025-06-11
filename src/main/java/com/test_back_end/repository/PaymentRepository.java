package com.test_back_end.repository;

import com.test_back_end.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findByPaymentNumberContainingIgnoreCase(String paymentNumber, Pageable pageable);
    Optional<Payment> findBySecureId(String secureId);
}

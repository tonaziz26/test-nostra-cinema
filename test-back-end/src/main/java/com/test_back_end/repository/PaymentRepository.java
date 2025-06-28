package com.test_back_end.repository;

import com.test_back_end.entity.Payment;
import com.test_back_end.entity.sql_response.PaymentSql;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p " +
            "INNER JOIN Transaction t ON t.payment.id = p.id " +
            "INNER JOIN Account a ON a.id = t.account.id " +
            "where p.secureId = :secureId " +
            "and a.email = :email")
    Optional<Payment> findBySecureIdAndEmail(String secureId, String email);

    Page<Payment> findByPaymentNumberContainingIgnoreCase(String paymentNumber, Pageable pageable);


    @Query("select p from Payment p " +
            "INNER JOIN Transaction t ON t.payment.id = p.id " +
            "INNER JOIN Account a ON a.id = t.account.id " +
            "where Lower(p.paymentNumber) like Lower(Concat('%',:paymentNumber,'%')) " +
            "and a.email = :email")
    Page<Payment> findByPaymentNumberContainingIgnoreCaseAndEmail(String paymentNumber, String email, Pageable pageable);



    Optional<Payment> findBySecureId(String secureId);

    @Query("SELECT new com.test_back_end.entity.sql_response.PaymentSql(" +
            "p.secureId, p.paymentNumber, p.status, p.expiredTime, p.bookingDate, p.totalPrice, " +
            "CONCAT(th.name, ' - ', c.name), s.number, t.secureId, t.chairNumber, a.name, p.paymentDate) " +
            "FROM Payment p " +
            "INNER JOIN Transaction t ON t.payment.id = p.id " +
            "INNER JOIN Account a ON a.id = t.account.id " +
            "INNER JOIN SessionMovie sm ON p.sessionMovie.id = sm.id " +
            "INNER JOIN StudioSession ss ON sm.studioSession.id = ss.id " +
            "INNER JOIN Studio s ON ss.studio.id = s.id " +
            "INNER JOIN Theater th ON th.id = s.theater.id " +
            "INNER JOIN City c ON c.id = th.city.id " +
            "WHERE p.secureId = :secureId")
    List<PaymentSql> findByPaymentId(@Param("secureId") String secureId);

    @Query("SELECT new com.test_back_end.entity.sql_response.PaymentSql(" +
            "p.secureId, p.paymentNumber, p.status, p.expiredTime, p.bookingDate, p.totalPrice, " +
            "CONCAT(th.name, ' - ', c.name), s.number, t.secureId, t.chairNumber, a.name, p.paymentDate) " +
            "FROM Payment p " +
            "INNER JOIN Transaction t ON t.payment.id = p.id " +
            "INNER JOIN Account a ON a.id = t.account.id " +
            "INNER JOIN SessionMovie sm ON p.sessionMovie.id = sm.id " +
            "INNER JOIN StudioSession ss ON sm.studioSession.id = ss.id " +
            "INNER JOIN Studio s ON ss.studio.id = s.id " +
            "INNER JOIN Theater th ON th.id = s.theater.id " +
            "INNER JOIN City c ON c.id = th.city.id " +
            "WHERE p.secureId = :secureId " +
            "AND a.email = :email")
    List<PaymentSql> findByPaymentIdAndEmail(@Param("secureId") String secureId, @Param("email") String email);
}

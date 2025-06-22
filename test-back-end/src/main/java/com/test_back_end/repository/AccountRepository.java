package com.test_back_end.repository;

import com.test_back_end.entity.Account;
import com.test_back_end.entity.sql_response.AccountSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findBySecureId(String secureId);


    Optional<Account> findByEmail(String email);


    @Query("SELECT a " +
            "FROM Account a " +
            "INNER JOIN UserLogin u on u.account.id = a.id " +
            "WHERE u.sessionId = :sessionId ")
    Optional<Account> findBySessionId(String sessionId);
}

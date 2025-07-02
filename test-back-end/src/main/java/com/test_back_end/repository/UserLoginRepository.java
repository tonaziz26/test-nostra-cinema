package com.test_back_end.repository;

import com.test_back_end.entity.UserLogin;
import com.test_back_end.entity.sql_response.AccountSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

    @Query("SELECT new com.test_back_end.entity.sql_response.AccountSQL(" +
            "a.id, a.name, u.sessionId, u.otp, u.expiredTime, r.name, u.isUsed) " +
            "FROM UserLogin u " +
            "INNER JOIN Account a on u.account.id = a.id " +
            "INNER JOIN Role r on a.role.id = r.id " +
            "WHERE u.sessionId = :sessionId ")
    Optional<AccountSQL> findBySessionIdAndPassword(String sessionId);

    Optional<UserLogin> findBySessionId(String sessionId);
}

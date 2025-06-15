package com.test_back_end.repository;

import com.test_back_end.entity.Account;
import com.test_back_end.entity.sql_response.AccountSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findBySecureId(String secureId);

    @Query("SELECT new com.test_back_end.entity.sql_response.AccountSQL(" +
            "a.id, a.name, a.email, a.password, a.expiredTime, r.name) " +
            "FROM Account a " +
            "INNER JOIN Role r on a.role.id = r.id " +
            "WHERE a.email = :email " +
            "AND a.password = :password")
    Optional<AccountSQL> findByEmailAndPassword(String email, String password);

    Optional<Account> findByEmail(String email);
}

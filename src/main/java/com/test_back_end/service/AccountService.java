package com.test_back_end.service;

import com.test_back_end.dto.AccountResponseDTO;
import com.test_back_end.entity.Account;
import com.test_back_end.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponseDTO getAccountDetailBySecureId(String secureId) {
        Account account = accountRepository.findBySecureId(secureId)
                .orElseThrow(() -> new RuntimeException("Account not found with secureId: " + secureId));
        
        return mapToResponseDTO(account);
    }
    
    private AccountResponseDTO mapToResponseDTO(Account account) {
        return new AccountResponseDTO(
                account.getSecureId(),
                account.getName(),
                account.getEmail()
        );
    }
}

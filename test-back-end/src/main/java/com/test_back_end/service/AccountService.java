package com.test_back_end.service;

import com.test_back_end.dto.AccountResponseDTO;
import com.test_back_end.entity.Account;
import com.test_back_end.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;

    public AccountService(AccountRepository accountRepository, EmailService emailService) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
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

    public void generateOTP(String email) throws Exception {
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account == null) return;

        Random random = new Random();
        int number = random.nextInt(1_000_000);

        account.setPassword(String.valueOf(number));
        account.setExpiredTime(LocalDateTime.now().plusMinutes(5));
        accountRepository.save(account);
        
        emailService.sendEmail(email, number);
    }
}

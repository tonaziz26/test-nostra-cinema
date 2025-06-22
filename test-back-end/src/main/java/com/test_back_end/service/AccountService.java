package com.test_back_end.service;

import com.test_back_end.dto.AccountResponseDTO;
import com.test_back_end.entity.Account;
import com.test_back_end.entity.UserLogin;
import com.test_back_end.repository.AccountRepository;
import com.test_back_end.repository.UserLoginRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final UserLoginRepository userLoginRepository;

    public AccountService(AccountRepository accountRepository, EmailService emailService, UserLoginRepository userLoginRepository) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.userLoginRepository = userLoginRepository;
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

    public void generateOTP(String email, String sessionId) {
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (account == null) return;

        if (null != account.getBlockUntil() && LocalDateTime.now().isBefore(account.getBlockUntil())) throw new RuntimeException("Account is blocked");


        Random random = new Random();
        int number = random.nextInt(1_000_000);

        UserLogin userLogin = new UserLogin();
        userLogin.setAccount(account);
        userLogin.setOtp(String.valueOf(number));
        userLogin.setUsed(false);
        userLogin.setSessionId(sessionId);
        userLogin.setExpiredTime(LocalDateTime.now().plusMinutes(5));
        userLoginRepository.save(userLogin);

        account.setBlockUntil(null);
        account.setLoginNumber(0);
        accountRepository.save(account);

        emailService.sendEmail(email, number);
    }
}

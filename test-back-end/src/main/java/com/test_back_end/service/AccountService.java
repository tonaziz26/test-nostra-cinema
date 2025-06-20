package com.test_back_end.service;

import com.test_back_end.dto.AccountResponseDTO;
import com.test_back_end.entity.Account;
import com.test_back_end.repository.AccountRepository;
import com.test_back_end.repository.RoleRepository;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class AccountService {

    private static final Logger logger = Logger.getLogger(AccountService.class.getName());

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final Session session;


    @Autowired
    public AccountService(AccountRepository accountRepository, RoleRepository roleRepository, Session session) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.session = session;
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
        logger.info("Generating OTP for email: " + email);

        Account account = accountRepository.findByEmail(email).orElse(new Account());

        if (null == account.getSecureId()) {
            account.setSecureId(UUID.randomUUID().toString());
            account.setEmail(email);
            account.setName("Guest");
            account.setRole(roleRepository.findByName("ROLE_USER"));
        }


        Random random = new Random();
        int number = random.nextInt(1_000_000);

        account.setPassword(String.valueOf(number));

        account.setExpiredTime(LocalDateTime.now().plusMinutes(5));

        accountRepository.save(account);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("admin-nostracinema@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("OTP code Nostra Cinema");
        String msg = "This is the OTP code nostra Cinema: " + account.getPassword() + " Please do not share this code with anyone.";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);

    }
}

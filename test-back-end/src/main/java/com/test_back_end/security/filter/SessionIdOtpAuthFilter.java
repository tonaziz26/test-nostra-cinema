package com.test_back_end.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_back_end.entity.Account;
import com.test_back_end.entity.sql_response.AccountSQL;
import com.test_back_end.repository.AccountRepository;
import com.test_back_end.repository.UserLoginRepository;
import com.test_back_end.security.LoginRequestDto;
import com.test_back_end.security.hendler.FailedOtpHandler;
import com.test_back_end.security.hendler.SuccessHandler;
import com.test_back_end.util.EncryptionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class SessionIdOtpAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final SuccessHandler successHandler;
    private final FailedOtpHandler failedHandler;
    private final ObjectMapper objectMapper;
    private final UserLoginRepository userLoginRepository;
    private final AccountRepository accountRepository;

    public SessionIdOtpAuthFilter(String defaultFilterProcessesUrl,
                                  SuccessHandler successHandler,
                                  FailedOtpHandler failedHandler,
                                  ObjectMapper objectMapper,
                                  UserLoginRepository userLoginRepository,
                                  AccountRepository accountRepository) {
        super(defaultFilterProcessesUrl);
        this.successHandler = successHandler;
        this.failedHandler = failedHandler;
        this.objectMapper = objectMapper;
        this.userLoginRepository = userLoginRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
            LoginRequestDto loginDto = objectMapper.readValue(request.getReader(), LoginRequestDto.class);

            if (loginDto.sessionId() == null || loginDto.otp() == null) {
                sendMessage(request, response, "Session ID and OTP are required");
            }

            AccountSQL account = userLoginRepository.findBySessionIdAndPassword(loginDto.sessionId())
                    .orElse(null);

            if (account == null || !EncryptionUtil.decrypt(account.getOtp()).equals(loginDto.otp())) {
                sendMessage(request, response, validateOtp(loginDto.sessionId()));
            }

            if (account != null && account.getUsed()) {
                sendMessage(request, response, "OTP has already used");
            }

            if (account != null && account.getExpiredTime().isBefore(LocalDateTime.now())) {
                sendMessage(request, response, "OTP has expired");
            }

            return this.getAuthenticationManager().authenticate(account);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.failedHandler.onAuthenticationFailure(request, response, failed);
    }

    public String validateOtp(String sessionId) {
        Optional<Account> accountOptional = accountRepository.findBySessionId(sessionId);
        String errorMessage = null;

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            if (account.getLoginNumber() < 2) {
                account.setLoginNumber(account.getLoginNumber() + 1);
                errorMessage = String.format("OTP verification failed, you have %d attempts left", 3 - account.getLoginNumber());
            } else {
                account.setBlockUntil(LocalDateTime.now().plusDays(1));
                account.setLoginNumber(0);
                errorMessage = "You have been blocked, please try again in after 24 hours";
            }

            accountRepository.save(account);

        }

        return errorMessage;
    }

    private void sendMessage(HttpServletRequest request, HttpServletResponse response, String messages) throws ServletException, IOException {
        request.setAttribute("message", messages);
        unsuccessfulAuthentication(request, response, null);
    }
}

package com.test_back_end.controller;

import com.test_back_end.dto.AccountResponseDTO;
import com.test_back_end.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/account")
@SecurityRequirement(name = "Bearer Authentication")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<AccountResponseDTO> getDetailAccount() {
        AccountResponseDTO accountResponse = accountService.getAccountDetail();
        return ResponseEntity.ok(accountResponse);
    }
}

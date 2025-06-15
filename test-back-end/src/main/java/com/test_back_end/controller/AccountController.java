package com.test_back_end.controller;

import com.test_back_end.dto.AccountResponseDTO;
import com.test_back_end.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{secureId}")
    public ResponseEntity<AccountResponseDTO> getDetailAccount(@PathVariable String secureId) {
        AccountResponseDTO accountResponse = accountService.getAccountDetailBySecureId(secureId);
        return ResponseEntity.ok(accountResponse);
    }
}

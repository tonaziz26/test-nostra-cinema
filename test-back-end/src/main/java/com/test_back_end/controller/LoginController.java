package com.test_back_end.controller;

import com.test_back_end.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/login")
public class LoginController {

    private final AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/generate-otp")
    public ResponseEntity<Boolean> getDetailAccount(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(accountService.generateOTP(email));
    }
}

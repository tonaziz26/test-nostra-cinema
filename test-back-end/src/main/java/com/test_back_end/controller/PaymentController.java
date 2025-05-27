package com.test_back_end.controller;

import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.dto.PaymentDTO;
import com.test_back_end.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<PageResultDTO<PaymentDTO>> getPaymentList(
            @RequestParam(name = "paymentNumber", defaultValue = "", required = false) String paymentNumber,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "paymentNumber") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        
        return ResponseEntity.ok(paymentService.getPaymentsByPaymentNumber(paymentNumber, page, size, sort, direction));
    }
}

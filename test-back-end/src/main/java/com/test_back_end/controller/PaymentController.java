package com.test_back_end.controller;

import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.dto.PaymentDTO;
import com.test_back_end.dto.PaymentDetailDTO;
import com.test_back_end.dto.request.PaymentApprovalDTO;
import com.test_back_end.dto.request.PaymentRequestDTO;
import com.test_back_end.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payments")
@SecurityRequirement(name = "Bearer Authentication")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<PageResultDTO<PaymentDTO>> getPaymentList(
            @RequestParam(name = "paymentNumber", defaultValue = "", required = false) String paymentNumber,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "paymentNumber") String sort,
            @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        
        return ResponseEntity.ok(paymentService.getPaymentsByPaymentNumber(paymentNumber, page, size, sort, direction));
    }
    
    @GetMapping("/{secureId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<PaymentDetailDTO> getDetailPayment(@PathVariable String secureId) {
        return ResponseEntity.ok(paymentService.getPaymentBySecureId(secureId));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentDTO paymentDTO = paymentService.createPayment(paymentRequestDTO);
        return new ResponseEntity<>(paymentDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{secureId}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PaymentDTO> updatePaymentStatus(@PathVariable String secureId,
                                                          @Valid @RequestBody PaymentApprovalDTO approvalDTO) {
        PaymentDTO paymentDTO = paymentService.approvalPayment(secureId, approvalDTO);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

    @PutMapping("/{secureId}/cancellation")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<PaymentDTO> cancelPayment(@PathVariable String secureId) {
        PaymentDTO paymentDTO = paymentService.cancelPayment(secureId);
        return new ResponseEntity<>(paymentDTO, HttpStatus.OK);
    }

}

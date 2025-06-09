package com.test_back_end.service;

import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.dto.PaymentDTO;
import com.test_back_end.dto.PaymentDetailDTO;
import com.test_back_end.dto.TransactionDTO;
import com.test_back_end.dto.request.PaymentRequestDTO;
import com.test_back_end.dto.request.TransactionRequestDTO;
import com.test_back_end.entity.Account;
import com.test_back_end.entity.Payment;
import com.test_back_end.entity.StudioSession;
import com.test_back_end.entity.Transaction;
import com.test_back_end.enums.PaymentStatus;
import com.test_back_end.repository.AccountRepository;
import com.test_back_end.repository.PaymentRepository;
import com.test_back_end.repository.StudioSessionRepository;
import com.test_back_end.repository.TransactionRepository;
import com.test_back_end.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private StudioSessionRepository studioSessionRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    public PageResultDTO<PaymentDTO> getPaymentsByPaymentNumber(String paymentNumber, int page, int limit, String sort, String direction) {
        Sort.Direction dir = PaginationUtil.getSortDirection(direction);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(dir, sort)));

        Page<Payment> paymentPage = paymentRepository.findByPaymentNumberContainingIgnoreCase(paymentNumber, pageable);

        List<PaymentDTO> paymentDTOs = paymentPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResultDTO<>(paymentDTOs, paymentPage.getNumber(), paymentPage.getTotalElements());
    }

    public PaymentDetailDTO getPaymentBySecureId(String secureId) {
        Payment payment = paymentRepository.findBySecureId(secureId)
                .orElseThrow(() -> new RuntimeException("Payment not found with secureId: " + secureId));

        PaymentDetailDTO dto = new PaymentDetailDTO();
        dto.setSecureId(payment.getSecureId());
        dto.setPaymentNumber(payment.getPaymentNumber());
        dto.setStatus(payment.getStatus());

        if (payment.getStatus().equals(PaymentStatus.WAITING_FOR_PAYMENT) && LocalDateTime.now().isAfter(payment.getExpiredTime())) {
            payment.setStatus(PaymentStatus.EXPIRED);
        }
        dto.setExpiredTime(payment.getExpiredTime());
        dto.setBookingDate(payment.getBookingDate());
        dto.setTotalPrice(payment.getTotalPrice());

        dto.setTransactions(payment.getTransactions().stream()
                .map(transaction -> {
                    TransactionDTO transactionDTO = new TransactionDTO();
                    transactionDTO.setSecureId(transaction.getSecureId());
                    transactionDTO.setAccountName(transaction.getAccount().getName());
                    transactionDTO.setChairNumber(transaction.getChairNumber());
                    return transactionDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }
    
    @Transactional
    public PaymentDTO createPayment(PaymentRequestDTO paymentRequestDTO) {
        Payment payment = new Payment();
        payment.setSecureId(UUID.randomUUID().toString());
        payment.setStatus(PaymentStatus.WAITING_FOR_PAYMENT);
        
        LocalDateTime bookingDate = Instant.ofEpochMilli(paymentRequestDTO.getBookingDateEpoch())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        payment.setBookingDate(bookingDate);
        
        payment.setExpiredTime(bookingDate.minusMinutes(10));
        
        BigDecimal totalPrice = paymentRequestDTO.getTransactions().stream()
                .map(TransactionRequestDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        payment.setTotalPrice(totalPrice);

        payment.setPaymentNumber(generatePaymentNumber(payment.getSecureId()) );

        payment = paymentRepository.save(payment);
        
        List<Transaction> transactions = new ArrayList<>();
        for (TransactionRequestDTO transactionDTO : paymentRequestDTO.getTransactions()) {
            Transaction transaction = new Transaction();
            transaction.setSecureId(UUID.randomUUID().toString());
            
            Account account = accountRepository.findBySecureId(transactionDTO.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found with ID: " + transactionDTO.getAccountId()));
            transaction.setAccount(account);
            
            StudioSession studioSession = studioSessionRepository.findById(transactionDTO.getStudioSessionId())
                    .orElseThrow(() -> new RuntimeException("Studio Session not found with ID: " + transactionDTO.getStudioSessionId()));
            transaction.setStudioSession(studioSession);
            
            transaction.setChairNumber(transactionDTO.getChairNumber());
            transaction.setPrice(transactionDTO.getPrice());
            transaction.setPayment(payment);
            
            transactions.add(transaction);
        }
        
        transactionRepository.saveAll(transactions);
        
        return convertToDTO(payment);
    }

    private String generatePaymentNumber(String secureId) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.getYear() +
                String.format("%02d", now.getMonthValue()) +
                String.format("%02d", now.getDayOfMonth());

        return "PAY-" + timestamp + "-" + secureId.substring(secureId.length() - 6);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setSecureId(payment.getSecureId());
        dto.setPaymentNumber(payment.getPaymentNumber());
        dto.setStatus(payment.getStatus());

        if (payment.getStatus().equals(PaymentStatus.WAITING_FOR_PAYMENT) && LocalDateTime.now().isAfter(payment.getExpiredTime())) {
            payment.setStatus(PaymentStatus.EXPIRED);
        }
        dto.setExpiredTime(payment.getExpiredTime());
        dto.setBookingDate(payment.getBookingDate());
        dto.setTotalPrice(payment.getTotalPrice());
        return dto;
    }
}

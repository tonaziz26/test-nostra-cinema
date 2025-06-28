package com.test_back_end.service;

import com.test_back_end.dto.PageResultDTO;
import com.test_back_end.dto.PaymentDTO;
import com.test_back_end.dto.PaymentDetailDTO;
import com.test_back_end.dto.TransactionDTO;
import com.test_back_end.dto.request.PaymentApprovalDTO;
import com.test_back_end.dto.request.PaymentRequestDTO;
import com.test_back_end.dto.request.TransactionRequestDTO;
import com.test_back_end.entity.*;
import com.test_back_end.entity.sql_response.PaymentSql;
import com.test_back_end.enums.PaymentStatus;
import com.test_back_end.repository.*;
import com.test_back_end.util.AuthUtil;
import com.test_back_end.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private TransactionRepository transactionRepository;
    @Autowired
    private SessionMovieRepository sessionMovieRepository;

    public PageResultDTO<PaymentDTO> getPaymentsByPaymentNumber(String paymentNumber, int page, int limit, String sort,
                                                                String direction) {
        Sort.Direction dir = PaginationUtil.getSortDirection(direction);
        Pageable pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(dir, sort)));

        Page<Payment> paymentPage;

        if ("ROLE_ADMIN".equals(AuthUtil.getRole())) {
            paymentPage = paymentRepository.findByPaymentNumberContainingIgnoreCase(paymentNumber, pageable);
        } else {
            paymentPage = paymentRepository.findByPaymentNumberContainingIgnoreCaseAndEmail(paymentNumber,
                    AuthUtil.getEmail(), pageable);
        }


        List<PaymentDTO> paymentDTOs = paymentPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResultDTO<>(paymentDTOs, paymentPage.getNumber(), paymentPage.getTotalElements());
    }

    public PaymentDetailDTO getPaymentBySecureId(String secureId) {
        List<PaymentSql> paymentSqlList;

        if ("ROLE_ADMIN".equals(AuthUtil.getRole())) {
            paymentSqlList = paymentRepository.findByPaymentId(secureId);
        } else {
            paymentSqlList = paymentRepository.findByPaymentIdAndEmail(secureId, AuthUtil.getEmail());
        }

        PaymentSql payment = paymentSqlList.get(0);

        PaymentDetailDTO dto = new PaymentDetailDTO();
        dto.setSecureId(payment.getSecureId());
        dto.setPaymentNumber(payment.getPaymentNumber());
        dto.setStatus(payment.getStatus());

        if (payment.getStatus().equals(PaymentStatus.WAITING_FOR_PAYMENT) &&
                LocalDateTime.now().isAfter(payment.getExpiredTime())) {
            dto.setStatus(PaymentStatus.EXPIRED);
        }
        dto.setExpiredTime(payment.getExpiredTime());
        dto.setBookingDate(payment.getBookingDate());
        dto.setTotalPrice(payment.getTotalPrice());
        dto.setStudio(payment.getStudioNumber());
        dto.setLocation(payment.getLocation());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setAccountName(payment.getUserName());

        dto.setTransactions(paymentSqlList.stream()
                .map(transaction -> {
                    TransactionDTO transactionDTO = new TransactionDTO();
                    transactionDTO.setSecureId(transaction.getTransactionId());
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


        payment.setTotalPrice(paymentRequestDTO.getTotalPrice());

        SessionMovie sessionMovie = sessionMovieRepository.findById(paymentRequestDTO.getSessionMovieId())
                .orElseThrow(() -> new RuntimeException("Studio Session not found with ID: " +
                        paymentRequestDTO.getSessionMovieId()));

        payment.setSessionMovie(sessionMovie);

        payment.setBookingDate(sessionMovie.getSessionDate());
        payment.setExpiredTime(sessionMovie.getSessionDate().minusMinutes(10));

        payment.setPaymentNumber(generatePaymentNumber(payment.getSecureId()));

        payment = paymentRepository.save(payment);

        String email = AuthUtil.getEmail();

        Account currentUserAccount = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found with email: " + email));

        List<Transaction> transactions = new ArrayList<>();
        for (TransactionRequestDTO transactionDTO : paymentRequestDTO.getTransactions()) {
            Transaction transaction = new Transaction();
            transaction.setSecureId(UUID.randomUUID().toString());

            transaction.setAccount(currentUserAccount);

            transaction.setChairNumber(transactionDTO.getChairNumber());
            transaction.setPrice(transactionDTO.getPrice());
            transaction.setPayment(payment);

            transactions.add(transaction);
        }

        transactionRepository.saveAll(transactions);

        return convertToDTO(payment);
    }

    public PaymentDTO approvalPayment(String secureId, PaymentApprovalDTO approvalDTO) {
        Payment payment = paymentRepository.findBySecureId(secureId)
                .orElseThrow(() -> new RuntimeException("Payment not found with secureId: " + secureId));

        if (!payment.getStatus().equals(PaymentStatus.WAITING_FOR_PAYMENT)) {
            throw new RuntimeException("Payment cannot be updated with secureId: " + secureId);
        }

        if (LocalDateTime.now().isAfter(payment.getExpiredTime()) &&
                !approvalDTO.getStatus().equals(PaymentStatus.SUCCESS)) {
            throw new RuntimeException("Payment was expired with secureId: " + secureId);
        }

        payment.setStatus(approvalDTO.getStatus());
        if (approvalDTO.getStatus().equals(PaymentStatus.SUCCESS)) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        paymentRepository.save(payment);

        return convertToDTO(payment);
    }


    public PaymentDTO cancelPayment(String secureId) {
        Payment payment;
        if ("ROLE_ADMIN".equals(AuthUtil.getRole())) {
            payment = paymentRepository.findBySecureId(secureId)
                    .orElseThrow(() -> new RuntimeException("Payment not found with secureId: " + secureId));
        } else {
            payment = paymentRepository.findBySecureIdAndEmail(secureId, AuthUtil.getEmail())
                    .orElseThrow(() -> new RuntimeException("Payment not found with secureId: " + secureId));
        }

        if (!payment.getStatus().equals(PaymentStatus.WAITING_FOR_PAYMENT)) {
            throw new RuntimeException("Payment cannot be canceled with secureId: " + secureId);
        }

        payment.setStatus(PaymentStatus.CANCELED);
        paymentRepository.save(payment);

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

        if (payment.getStatus().equals(PaymentStatus.WAITING_FOR_PAYMENT) &&
                LocalDateTime.now().isAfter(payment.getExpiredTime())) {
            dto.setStatus(PaymentStatus.EXPIRED);
        }
        dto.setExpiredTime(payment.getExpiredTime());
        dto.setBookingDate(payment.getBookingDate());
        dto.setTotalPrice(payment.getTotalPrice());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}

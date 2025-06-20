package com.test_back_end.validation;

import com.test_back_end.dto.request.PaymentRequestDTO;
import com.test_back_end.dto.request.TransactionRequestDTO;
import com.test_back_end.entity.Transaction;
import com.test_back_end.repository.TransactionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionNotBookedValidator implements ConstraintValidator<TransactionNotBooked, PaymentRequestDTO> {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean isValid(PaymentRequestDTO paymentRequestDTO, ConstraintValidatorContext context) {

        List<String> chairNumbers = paymentRequestDTO.getTransactions().stream()
                .map(TransactionRequestDTO::getChairNumber)
                .collect(Collectors.toList());

        List<Transaction> existingTransactions = transactionRepository.findByPaymentBookingDateAndSessionId(paymentRequestDTO.getSessionMovieId(), chairNumbers);

        if (existingTransactions.isEmpty()) return true;

        List<String> conflictingTransactions = new ArrayList<>();

        existingTransactions.forEach(transaction -> {
            conflictingTransactions.add(transaction.getChairNumber());
        });

        context.disableDefaultConstraintViolation();

        String errorMessage = "Chair(s) already booked: " + String.join(", ", conflictingTransactions);

        context.buildConstraintViolationWithTemplate(errorMessage)
                .addConstraintViolation();

        return false;
    }
}

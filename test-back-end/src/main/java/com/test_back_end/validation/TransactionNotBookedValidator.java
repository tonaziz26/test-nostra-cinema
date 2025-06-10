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

public class TransactionNotBookedValidator implements ConstraintValidator<TransactionNotBooked, PaymentRequestDTO> {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public boolean isValid(PaymentRequestDTO paymentRequestDTO, ConstraintValidatorContext context) {

        LocalDateTime bookingDate = Instant.ofEpochMilli(paymentRequestDTO.getBookingDateEpoch())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        List<Transaction> existingTransactions = transactionRepository.findByPaymentBookingDateAndSessionId(bookingDate, paymentRequestDTO.getStudioSessionId());
        
        if (existingTransactions.isEmpty()) {
            return true;
        }

        List<String> conflictingTransactions = new ArrayList<>();
        
        for (TransactionRequestDTO transactionDTO : paymentRequestDTO.getTransactions()) {
            String chairNumber = transactionDTO.getChairNumber();


            boolean alreadyBooked = existingTransactions.stream()
                    .anyMatch(transaction -> 
                        transaction.getChairNumber().equalsIgnoreCase(chairNumber)
                    );
            
            if (alreadyBooked) {
                conflictingTransactions.add(chairNumber);
            }
        }
        
        if (!conflictingTransactions.isEmpty()) {
            context.disableDefaultConstraintViolation();
            
            String errorMessage = "Chair(s) already booked: " + String.join(", ", conflictingTransactions);
            
            context.buildConstraintViolationWithTemplate(errorMessage)
                   .addConstraintViolation();
            
            return false;
        }
        
        return true;
    }
}

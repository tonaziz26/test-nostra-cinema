package com.test_back_end.validation;

import com.test_back_end.dto.request.PaymentRequestDTO;
import com.test_back_end.dto.request.TransactionRequestDTO;
import com.test_back_end.entity.LayoutStudio;
import com.test_back_end.repository.LayoutStudioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AvailableChairValidator implements ConstraintValidator<AvailableChair, PaymentRequestDTO> {

    @Autowired
    private LayoutStudioRepository layoutStudioRepository;

    @Override
    public boolean isValid(PaymentRequestDTO paymentRequestDTO, ConstraintValidatorContext context) {

        List<String> invalidChairs = new ArrayList<>();

        for (TransactionRequestDTO transaction : paymentRequestDTO.getTransactions()) {
            String chairNumber = transaction.getChairNumber();
            
            Optional<LayoutStudio> layoutStudio = layoutStudioRepository.findBySessionMovieIdAndChairNumber(
                    paymentRequestDTO.getSessionMovieId(), chairNumber);
            
            if (layoutStudio.isEmpty()) {
                invalidChairs.add(chairNumber);
            }
        }

        if (!invalidChairs.isEmpty()) {
            context.disableDefaultConstraintViolation();
            String errorMessage = "Chair(s) do not exist in this studio: " + String.join(", ", invalidChairs);
            context.buildConstraintViolationWithTemplate(errorMessage)
                  .addConstraintViolation();
            return false;
        }

        return true;
    }
}
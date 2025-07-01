package com.test_back_end.validation;

import com.test_back_end.dto.request.PaymentRequestDTO;
import com.test_back_end.dto.request.TransactionRequestDTO;
import com.test_back_end.entity.LayoutStudio;
import com.test_back_end.repository.LayoutStudioRepository;
import com.test_back_end.service.LayoutStudioService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class LayoutPositionValidator implements ConstraintValidator<LayoutPosition, PaymentRequestDTO> {

    @Autowired
    private LayoutStudioService layoutStudioService;

    @Autowired
    private LayoutStudioRepository layoutStudioRepository;

    @Override
    public boolean isValid(PaymentRequestDTO paymentRequestDTO, ConstraintValidatorContext context) {
        List<String> chairNumbers = paymentRequestDTO.getTransactions()
                .stream()
                .map(TransactionRequestDTO::getChairNumber)
                .collect(Collectors.toList());

        List<LayoutStudio> layoutStudios = layoutStudioRepository
                .findBySessionMovieIdAndNewChairNumber(
                        paymentRequestDTO.getSessionMovieId(),
                        chairNumbers
                );

        return layoutStudios.isEmpty();
    }

}

package com.test_back_end.validation;

import com.test_back_end.dto.MovieRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, MovieRequestDTO> {

    @Override
    public boolean isValid(MovieRequestDTO movieRequestDTO, ConstraintValidatorContext context) {
        return movieRequestDTO.getEndDate() > movieRequestDTO.getStartDate();
    }
}

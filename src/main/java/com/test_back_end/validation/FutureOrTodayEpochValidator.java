package com.test_back_end.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class FutureOrTodayEpochValidator implements ConstraintValidator<FutureOrTodayEpoch, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {

        LocalDate inputDate = Instant.ofEpochMilli(value)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        return !inputDate.isBefore(today);
    }
}


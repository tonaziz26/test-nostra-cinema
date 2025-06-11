package com.test_back_end.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FutureOrTodayDateValidator implements ConstraintValidator<FutureOrTodayDate, String> {

    @Override
    public boolean isValid(String dateStr, ConstraintValidatorContext context) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            LocalDate today = LocalDate.now();
            return !date.isBefore(today);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

package com.test_back_end.validation;

import com.test_back_end.repository.MovieRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueMovieNameValidator implements ConstraintValidator<UniqueMovieName, String> {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        if (name == null || name.trim().isEmpty()) return true;
        return !movieRepository.existsByNameIgnoreCase(name.trim());
    }
}


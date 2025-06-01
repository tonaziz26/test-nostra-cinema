package com.test_back_end.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueMovieNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueMovieName {
    String message() default "Movie name already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
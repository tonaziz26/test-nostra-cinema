package com.test_back_end.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = AvailableChairValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AvailableChair {
    String message() default "Chair number does not exist in this studio";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

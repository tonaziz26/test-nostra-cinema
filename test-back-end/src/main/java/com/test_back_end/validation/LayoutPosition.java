package com.test_back_end.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LayoutPositionValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LayoutPosition {
    String message() default "invalid chair position layout position";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

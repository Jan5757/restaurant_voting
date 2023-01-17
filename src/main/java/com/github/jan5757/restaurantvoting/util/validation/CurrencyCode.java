package com.github.jan5757.restaurantvoting.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyCodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyCode {
    String message() default "{CurrencyCode.badCode}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

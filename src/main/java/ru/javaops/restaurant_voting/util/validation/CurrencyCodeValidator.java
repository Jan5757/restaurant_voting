package ru.javaops.restaurant_voting.util.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

public class CurrencyCodeValidator implements ConstraintValidator<CurrencyCode, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        try {
            Currency.getInstance(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}

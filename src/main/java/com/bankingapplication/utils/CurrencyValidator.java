package com.bankingapplication.utils;

import com.bankingapplication.constants.CURRENCY;
import com.bankingapplication.constants.ValidCurrency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;


public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public boolean isValid(String currencyCode, ConstraintValidatorContext context) {
        return currencyCode != null && Arrays.stream(CURRENCY.values()).anyMatch(c -> c.name().equals(currencyCode));
    }

}
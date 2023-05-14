package com.transaction.bankingapplication.utils;

import com.transaction.bankingapplication.constants.CURRENCY;
import com.transaction.bankingapplication.constants.ValidCurrency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;


public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public boolean isValid(String currencyCode, ConstraintValidatorContext context) {
        return currencyCode != null && Arrays.stream(CURRENCY.values()).anyMatch(c -> c.name().equals(currencyCode));
    }

}
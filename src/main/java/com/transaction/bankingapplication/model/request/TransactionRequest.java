package com.transaction.bankingapplication.model.request;

import com.transaction.bankingapplication.constants.ValidCurrency;
import com.transaction.bankingapplication.model.entity.Account;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotNull
    private String sourceAccount;

    @NotNull
    private String targetAccount;

    @Positive(message = "Transfer amount must be positive")
    // Prevent fraudulent transfers attempting to abuse currency conversion errors
    @Min(value = 1, message = "Amount must be larger than 1")
    private double amount;

    @ValidCurrency
    private String currency;


}

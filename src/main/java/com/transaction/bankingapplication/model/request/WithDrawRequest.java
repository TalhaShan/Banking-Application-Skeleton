package com.transaction.bankingapplication.model.request;

import com.transaction.bankingapplication.constants.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithDrawRequest {

    @NotNull
    @NotBlank(message = "Target account no is mandatory")
    private String targetAccountNo;

    @Positive(message = "Withdraw amount must be positive")
    // Prevent fraudulent transfers attempting to abuse currency conversion errors
    @Min(value = 1, message = "Amount must be larger than 1")
    private double amount;

    @ValidCurrency
    private String currency;



}

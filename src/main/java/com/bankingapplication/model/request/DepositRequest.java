package com.bankingapplication.model.request;

import com.bankingapplication.constants.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {

    @NotNull
    @NotBlank(message = "Target account no is mandatory")
    private String targetAccountNo;

    @Positive(message = "Deposit amount must be positive")
    // Prevent fraudulent transfers attempting to abuse currency conversion errors
    @Min(value = 1, message = "Amount must be larger than 1")
    private double amount;

    @ValidCurrency
    private String currency;



}

package com.transaction.bankingapplication.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;
    @Positive(message = "Transfer amount must be positive")
    private double balance;
}

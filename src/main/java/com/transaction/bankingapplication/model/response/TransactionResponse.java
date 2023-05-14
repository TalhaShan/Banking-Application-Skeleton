package com.transaction.bankingapplication.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TransactionResponse {

    private String account;
    private String message;
    private boolean isCompleted;
}

package com.bankingapplication.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatusResponse {

    private String account;
    private double balance;
}

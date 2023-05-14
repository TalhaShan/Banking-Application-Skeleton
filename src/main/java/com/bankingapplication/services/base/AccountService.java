package com.bankingapplication.services.base;

import com.bankingapplication.model.request.AccountCreationRequest;
import com.bankingapplication.model.entity.Account;

import com.bankingapplication.model.response.AccountStatusResponse;

public interface AccountService {

    Account createNewAccount(AccountCreationRequest accountCreationRequest);
    AccountStatusResponse checkAccountBalance(String accountNo);
}

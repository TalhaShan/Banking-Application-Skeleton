package com.transaction.bankingapplication.services.base;

import com.transaction.bankingapplication.model.entity.Account;
import com.transaction.bankingapplication.model.request.AccountCreationRequest;

import com.transaction.bankingapplication.model.response.AccountStatusResponse;

public interface AccountService {

    Account createNewAccount(AccountCreationRequest accountCreationRequest);
    AccountStatusResponse checkAccountBalance(String accountNo);
}

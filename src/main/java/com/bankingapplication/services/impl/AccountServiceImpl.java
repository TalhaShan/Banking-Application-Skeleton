package com.bankingapplication.services.impl;

import com.bankingapplication.model.request.AccountCreationRequest;
import com.bankingapplication.constants.CURRENCY;
import com.bankingapplication.model.entity.Account;

import com.bankingapplication.model.request.DepositRequest;
import com.bankingapplication.model.response.AccountStatusResponse;
import com.bankingapplication.repository.AccountRepository;
import com.bankingapplication.services.base.AccountService;
import com.bankingapplication.services.base.TransactionService;
import com.bankingapplication.utils.GenericUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


/**

 AccountServiceImpl is a service class that implements the
 AccountService interface to provide account-related functionalities.
 */
@Service
@NoArgsConstructor
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {


    @Autowired
    TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    /**
     * This method creates a new account with the provided account details and initial balance.
     * @param accountCreationRequest Object containing the account creation details.
     * @return Account Object of the newly created account.
     */
    @Transactional
    @Override
    public Account createNewAccount(AccountCreationRequest accountCreationRequest) {
        Account account = new Account();
        account.setAccountNumber(String.valueOf(UUID.randomUUID()));
        account.setBalance(0.0);
        account.setName(accountCreationRequest.getName());
        account.setIsActive(Boolean.TRUE);
        account.setCreationDate(LocalDateTime.now());
        accountRepository.save(account);
        transactionService.makeDeposit(DepositRequest.builder().amount(accountCreationRequest.getBalance()).targetAccountNo(account.getAccountNumber()).currency(CURRENCY.AED.name()).build(), GenericUtils.transactionReferenceNumber());

        return account;
    }

    /**
     * This method checks the balance of the provided account number.
     * @param accountNo String containing the account number to be checked.
     * @return AccountStatusResponse Object containing the account number and balance details.
     */
    @Override
    public AccountStatusResponse checkAccountBalance(String accountNo) {

        Optional<Account> account = accountRepository.findByAccountNumber(accountNo);
        if (account.isPresent()) {
            return AccountStatusResponse.builder().
                    account(account.get().getAccountNumber())
                    .balance(account.get().getBalance()).build();
        }
        return AccountStatusResponse.builder().build();
    }
}

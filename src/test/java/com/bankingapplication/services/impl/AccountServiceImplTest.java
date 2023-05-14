package com.bankingapplication.services.impl;


import com.bankingapplication.model.entity.Account;
import com.bankingapplication.model.request.AccountCreationRequest;
import com.bankingapplication.model.request.DepositRequest;
import com.bankingapplication.model.response.AccountStatusResponse;
import com.bankingapplication.repository.AccountRepository;
import com.bankingapplication.utils.InputValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private TransactionServiceImpl transactionService;

    @Mock
    private AccountRepository accountRepository;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(transactionService, accountRepository);
    }

    @Test
    void testCreateNewAccount() {
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest();
        accountCreationRequest.setName("Talha");
        accountCreationRequest.setBalance(100.0);

        Account account = new Account();
        account.setAccountNumber(String.valueOf(UUID.randomUUID()));
        account.setBalance(0.0);
        account.setName(accountCreationRequest.getName());
        account.setIsActive(Boolean.TRUE);
        account.setCreationDate(LocalDateTime.now());

        when(accountRepository.save(any(Account.class))).thenReturn(account);

      Account accountCreatedResponse =  accountService.createNewAccount(accountCreationRequest);

        verify(accountRepository, times(1)).save(any(Account.class));
        verify(transactionService, times(1)).makeDeposit(any(DepositRequest.class), anyString());

        assertNotNull(account.getAccountNumber());
        assertTrue(accountCreatedResponse.getIsActive());
        assertEquals("Talha", accountCreatedResponse.getName());


    }

    @Test
    void testCheckAccountBalance() {
        String accountNo = "1234567890";
        Double balance = 100.0;

        Account account = new Account();
        account.setAccountNumber(accountNo);
        account.setBalance(balance);

        when(accountRepository.findByAccountNumber(accountNo)).thenReturn(Optional.of(account));

        AccountStatusResponse accountStatusResponse = accountService.checkAccountBalance(accountNo);

        assertEquals(accountNo, accountStatusResponse.getAccount());
        assertEquals(balance, accountStatusResponse.getBalance());
    }
    @Test
     void testCreateNewAccountWithNullRequest() {
        // Arrange
        AccountCreationRequest request = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> accountService.createNewAccount(request));
    }

    @Test
     void testCreateNewAccountWithInvalidBalance() {
        // create an invalid request with negative balance
        AccountCreationRequest request = new AccountCreationRequest();
        request.setName("Talha Shan");
        request.setBalance(-100.0);

        // assert that the criteria validation fails
        Assertions.assertFalse(InputValidator.isCreateAccountCriteriaValid(request));

    }
    @Test
     void testCreateNewAccountWithFailedTransaction() {
        // Arrange
        AccountCreationRequest request = new AccountCreationRequest();
        request.setName("Talha");
        request.setBalance(1000.0);
        when(transactionService.makeDeposit(any(DepositRequest.class), anyString()))
                .thenThrow(new RuntimeException("Failed to deposit"));

        // Act and Assert
        assertThrows(RuntimeException.class, () -> accountService.createNewAccount(request));
    }

    @Test
     void testCheckAccountBalanceWithNonExistentAccount() {
        // Arrange
        String accountNo = "123456";
        when(accountRepository.findByAccountNumber(accountNo)).thenReturn(Optional.empty());

        // Act and Assert
        Account account = new Account();
        account.setAccountNumber(accountNo);
        AccountStatusResponse accountStatusResponse = accountService.checkAccountBalance(accountNo);
        assertNull(accountStatusResponse.getAccount());
        assertEquals(0.0,accountStatusResponse.getBalance());
    }
}


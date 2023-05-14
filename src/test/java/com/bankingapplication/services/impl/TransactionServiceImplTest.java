package com.bankingapplication.services.impl;

import com.bankingapplication.constants.Constants;
import com.bankingapplication.model.entity.Account;
import com.bankingapplication.model.entity.Transaction;
import com.bankingapplication.model.request.DepositRequest;
import com.bankingapplication.model.request.TransactionRequest;
import com.bankingapplication.model.request.WithDrawRequest;
import com.bankingapplication.model.response.TransactionResponse;
import com.bankingapplication.repository.AccountRepository;
import com.bankingapplication.repository.TransactionRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account sourceAccount;
    private Account targetAccount;

    @BeforeEach
    public void setUp() {
        sourceAccount = new Account(UUID.randomUUID().toString(), "Talha Shan", 100.0, LocalDateTime.now(), true);
        targetAccount = new Account(UUID.randomUUID().toString(), "Talha Shan2", 500.0, LocalDateTime.now(), true);
    }

    @Test
    void testMakeTransfer_Success() {
        TransactionRequest request = new TransactionRequest("123456", "654321", 200.0, "USD");
        sourceAccount.setBalance(1000.0);
        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("654321")).thenReturn(Optional.of(targetAccount));

        TransactionResponse response = transactionService.makeTransfer(request, "transaction-id-1");

        assertTrue(response.isCompleted());
        assertNotNull(response.getAccount());
        Assertions.assertEquals(Constants.SUCCESS, response.getMessage());

        verify(accountRepository, times(2)).findByAccountNumber(anyString());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testMakeTransfer_InsufficientBalance() {
        TransactionRequest request = new TransactionRequest("123456", "654321", 1000.0, "USD");
        sourceAccount.setBalance(10.0);
        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("654321")).thenReturn(Optional.of(targetAccount));

        TransactionResponse response = transactionService.makeTransfer(request, "transaction-id-2");

        assertFalse(response.isCompleted());
        assertNotNull(response.getAccount());
        assertEquals(Constants.INSUFFICIENT_ACCOUNT_BALANCE, response.getMessage());

        verify(accountRepository, times(2)).findByAccountNumber(anyString());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testMakeTransfer_InvalidAccounts() {
        TransactionRequest request = new TransactionRequest("123456", "999999", 100.0, "USD");

        when(accountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("999999")).thenReturn(Optional.empty());

        TransactionResponse response = transactionService.makeTransfer(request, "transaction-id-3");

        assertFalse(response.isCompleted());
        assertEquals("123456", response.getAccount());
        assertEquals(Constants.INVALID_TRANSACTION, response.getMessage());

        verify(accountRepository, times(2)).findByAccountNumber(anyString());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testMakeDeposit_Success() {
        DepositRequest request = new DepositRequest("4b17fff5-48e5-4ea4-92a5-46408edfd75e", 100.0, "USD");
        when(accountRepository.findByAccountNumber("4b17fff5-48e5-4ea4-92a5-46408edfd75e")).thenReturn(Optional.of(targetAccount));

        TransactionResponse response = transactionService.makeDeposit(request, "transaction-id-4");

        assertTrue(response.isCompleted());
        assertNotNull(response.getAccount());
        assertEquals(Constants.SUCCESS, response.getMessage());

        verify(accountRepository, times(1)).findByAccountNumber(anyString());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testMakeDeposit_NoAccountFound() {
        DepositRequest request = new DepositRequest("999999", 100.0, "USD");
        when(accountRepository.findByAccountNumber("999999")).thenReturn(Optional.empty());

        TransactionResponse response = transactionService.makeDeposit(request, "transaction-id-5");

        assertFalse(response.isCompleted());
        assertEquals("999999", response.getAccount());
        //   assertEquals

    }

    @Test
    void makeWithDraw_SuccessfulWithdrawal() {
        // Arrange
        WithDrawRequest request = new WithDrawRequest("4b17fff5-48e5-4ea4-92a5-46408edfd75e", 100.0, "USD");
        when(accountRepository.findByAccountNumber("4b17fff5-48e5-4ea4-92a5-46408edfd75e")).thenReturn(Optional.of(targetAccount));

        TransactionResponse response = transactionService.makeWithDraw(request, "transaction-id-4");

        assertTrue(response.isCompleted());
        assertNotNull(response.getAccount());
        assertEquals(Constants.SUCCESS, response.getMessage());

        verify(accountRepository, times(1)).findByAccountNumber(anyString());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void makeWithDraw_AccountNotFound() {
        // Arrange
        WithDrawRequest request = new WithDrawRequest("999999", 100.0, "USD");
        when(accountRepository.findByAccountNumber("999999")).thenReturn(Optional.empty());

        TransactionResponse response = transactionService.makeWithDraw(request, "transaction-id-5");

        assertFalse(response.isCompleted());
        assertEquals("999999", response.getAccount());

    }

    @Test
    void makeWithDraw_InsufficientBalance() {
        // Arrange
        String targetAccountNo = "1234567890";
        Double withdrawAmount = 10000.0;

        WithDrawRequest request = new WithDrawRequest(targetAccountNo, withdrawAmount, "USD");

        Mockito.when(accountRepository.findByAccountNumber(targetAccountNo)).thenReturn(Optional.of(targetAccount));


        TransactionResponse response = transactionService.makeWithDraw(request, "12fabc13ef");

        // Assert
        assertFalse(response.isCompleted());
        assertEquals(Constants.INSUFFICIENT_ACCOUNT_BALANCE, response.getMessage());
        assertNotNull(response.getAccount());
        assertEquals(500.0, targetAccount.getBalance());
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
    }

}
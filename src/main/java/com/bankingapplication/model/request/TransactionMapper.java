package com.bankingapplication.model.request;

import com.bankingapplication.model.entity.Account;
import com.bankingapplication.model.entity.Transaction;

import java.time.LocalDateTime;

public class TransactionMapper {


    public static Transaction createTransaction(double amount, Account sourceAccount, Account targetAccount, String currency, String transactionType) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setCurrency(currency);
        transaction.setInitiationDate(LocalDateTime.now());
        transaction.setCompletionDate(LocalDateTime.now());
        transaction.setTransactionType(transactionType);
        return transaction;
    }
}
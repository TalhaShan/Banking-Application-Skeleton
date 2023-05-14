package com.transaction.bankingapplication.services.impl;

import com.transaction.bankingapplication.constants.ACTION;
import com.transaction.bankingapplication.constants.Constants;
import com.transaction.bankingapplication.model.request.DepositRequest;
import com.transaction.bankingapplication.model.request.TransactionMapper;
import com.transaction.bankingapplication.model.request.WithDrawRequest;
import com.transaction.bankingapplication.model.response.TransactionResponse;
import com.transaction.bankingapplication.model.entity.Account;
import com.transaction.bankingapplication.model.entity.Transaction;
import com.transaction.bankingapplication.model.request.TransactionRequest;
import com.transaction.bankingapplication.repository.AccountRepository;
import com.transaction.bankingapplication.repository.TransactionRepository;
import com.transaction.bankingapplication.services.base.TransactionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;


/**
 * This class is an implementation of TransactionService interface,
 * providing functionality for money transfer, deposit and withdraw.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);


    /**
     * Performs money transfer between accounts.
     *
     * @param transactionRequest  Request containing source account, target account, currency and amount.
     * @param uniqueTransactionId Unique id for the transaction.
     * @return TransactionResponse containing response message and status.
     */
    @Transactional
    @Override
    public TransactionResponse makeTransfer(TransactionRequest transactionRequest, String uniqueTransactionId) {

        Optional<Account> sourceAccount = accountRepository
                .findByAccountNumber(transactionRequest.getSourceAccount());

        Optional<Account> targetAccount = accountRepository
                .findByAccountNumber(transactionRequest.getTargetAccount());

        TransactionResponse transactionResponse = new TransactionResponse();
        if (sourceAccount.isPresent() && targetAccount.isPresent() && sourceAccount.get().getIsActive() && targetAccount.get().getIsActive()) {
            if (isAmountAvailable(transactionRequest.getAmount(), sourceAccount.get().getBalance())) {

                Transaction transaction = TransactionMapper.createTransaction(transactionRequest.getAmount(),
                        sourceAccount.get(), targetAccount.get(),
                        transactionRequest.getCurrency(), ACTION.TRANSFER.name());

                updateSourceAndTargetAccountBalance(sourceAccount.get(), targetAccount.get(), transactionRequest.getAmount(), ACTION.TRANSFER);
                transactionRepository.save(transaction);

                transactionResponse.setAccount(sourceAccount.get().getAccountNumber());
                transactionResponse.setCompleted(Boolean.TRUE);
                transactionResponse.setMessage(Constants.SUCCESS);
                LOGGER.info("calling transfer service complete: {}", uniqueTransactionId);
                return transactionResponse;
            } else {
                transactionResponse.setAccount(sourceAccount.get().getAccountNumber());
                transactionResponse.setMessage(Constants.INSUFFICIENT_ACCOUNT_BALANCE);
                LOGGER.error("calling transfer service failed: {}", uniqueTransactionId);
                return transactionResponse;
            }
        }

        transactionResponse.setAccount(transactionRequest.getSourceAccount());
        transactionResponse.setCompleted(Boolean.FALSE);
        transactionResponse.setMessage(Constants.INVALID_TRANSACTION);
        LOGGER.error("calling transfer service failed: {}", uniqueTransactionId);
        return transactionResponse;
    }

    /**
     * Performs money deposit to an account.
     *
     * @param depositRequest      Request containing target account, currency and amount.
     * @param uniqueTransactionId Unique id for the transaction.
     * @return TransactionResponse containing response message and status.
     */
    @Transactional
    @Override
    public TransactionResponse makeDeposit(DepositRequest depositRequest, String uniqueTransactionId) {

        Optional<Account> targetAccount = accountRepository.findByAccountNumber(depositRequest.getTargetAccountNo());
        TransactionResponse transactionResponse = new TransactionResponse();
        if (targetAccount.isPresent() && targetAccount.get().getIsActive()) {

            Transaction transaction = TransactionMapper.createTransaction(depositRequest.getAmount(), targetAccount.get(), targetAccount.get(), depositRequest.getCurrency(), ACTION.DEPOSIT.name());

            updateAccountBalance(targetAccount.get(), depositRequest.getAmount(), ACTION.DEPOSIT);
            transactionRepository.save(transaction);

            transactionResponse.setAccount(targetAccount.get().getAccountNumber());
            transactionResponse.setCompleted(Boolean.TRUE);
            transactionResponse.setMessage(Constants.SUCCESS);
            LOGGER.info("calling deposit service complete: {}", uniqueTransactionId);
            return transactionResponse;

        }
        transactionResponse.setAccount(depositRequest.getTargetAccountNo());
        transactionResponse.setCompleted(Boolean.FALSE);
        transactionResponse.setMessage(Constants.NO_ACCOUNT_FOUND);
        LOGGER.error("calling deposit service failed: {}", uniqueTransactionId);
        return transactionResponse;
    }

    /**
     * Performs money deposit to an account.
     *
     * @param withDrawRequest     Request containing target account, currency and amount.
     * @param uniqueTransactionId Unique id for the transaction.
     * @return TransactionResponse containing response message and status.
     */
    @Override
    public TransactionResponse makeWithDraw(WithDrawRequest withDrawRequest, String uniqueTransactionId) {
        Optional<Account> targetAccount = accountRepository.findByAccountNumber(withDrawRequest.getTargetAccountNo());
        TransactionResponse transactionResponse = new TransactionResponse();
        if (targetAccount.isPresent() && targetAccount.get().getIsActive()) {

            if (isAmountAvailable(withDrawRequest.getAmount(), targetAccount.get().getBalance())) {

                Transaction transaction = TransactionMapper.createTransaction(withDrawRequest.getAmount(), targetAccount.get(), targetAccount.get(), withDrawRequest.getCurrency(), ACTION.WITHDRAW.name());
                updateAccountBalance(targetAccount.get(), withDrawRequest.getAmount(), ACTION.WITHDRAW);
                transactionRepository.save(transaction);

                transactionResponse.setAccount(targetAccount.get().getAccountNumber());
                transactionResponse.setCompleted(Boolean.TRUE);
                transactionResponse.setMessage(Constants.SUCCESS);
                LOGGER.info("calling withdraw service complete: {}", uniqueTransactionId);
                return transactionResponse;

            } else {

                transactionResponse.setAccount(targetAccount.get().getAccountNumber());
                transactionResponse.setCompleted(Boolean.FALSE);
                transactionResponse.setMessage(Constants.INSUFFICIENT_ACCOUNT_BALANCE);
                LOGGER.error("calling withdraw service failed: {}", uniqueTransactionId);
                return transactionResponse;
            }
        }

        transactionResponse.setAccount(withDrawRequest.getTargetAccountNo());
        transactionResponse.setMessage(Constants.NO_ACCOUNT_FOUND);
        LOGGER.error("calling deposit service failed: {}", uniqueTransactionId);
        return transactionResponse;
    }

    public void updateAccountBalance(Account account, double amount, ACTION action) {
        if (action == ACTION.WITHDRAW) {
            account.setBalance((account.getBalance() - amount));
        } else if (action == ACTION.DEPOSIT) {
            account.setBalance((account.getBalance() + amount));
        }
        accountRepository.save(account);
    }

    public void updateSourceAndTargetAccountBalance(Account sourceAccount, Account targetAccount, double amount, ACTION action) {
        if (action == ACTION.TRANSFER) {
            sourceAccount.setBalance((sourceAccount.getBalance() - amount));
            targetAccount.setBalance(targetAccount.getBalance() + amount);
            accountRepository.save(sourceAccount);
            accountRepository.save(targetAccount);
        }
    }

    public boolean isAmountAvailable(double amount, double accountBalance) {
        return (accountBalance - amount) > 0;
    }
}

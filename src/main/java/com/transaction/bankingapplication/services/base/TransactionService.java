package com.transaction.bankingapplication.services.base;

import com.transaction.bankingapplication.model.request.DepositRequest;
import com.transaction.bankingapplication.model.request.WithDrawRequest;
import com.transaction.bankingapplication.model.response.TransactionResponse;
import com.transaction.bankingapplication.model.request.TransactionRequest;

public interface TransactionService {

    TransactionResponse makeTransfer(TransactionRequest transactionRequest,String uniqueTransactionId);
    TransactionResponse makeDeposit(DepositRequest depositRequest,String uniqueTransactionId);
    TransactionResponse makeWithDraw(WithDrawRequest depositRequest,String uniqueTransactionId);
}

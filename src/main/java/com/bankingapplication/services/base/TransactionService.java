package com.bankingapplication.services.base;

import com.bankingapplication.model.request.TransactionRequest;
import com.bankingapplication.model.request.DepositRequest;
import com.bankingapplication.model.request.WithDrawRequest;
import com.bankingapplication.model.response.TransactionResponse;

public interface TransactionService {

    TransactionResponse makeTransfer(TransactionRequest transactionRequest, String uniqueTransactionId);
    TransactionResponse makeDeposit(DepositRequest depositRequest,String uniqueTransactionId);
    TransactionResponse makeWithDraw(WithDrawRequest depositRequest,String uniqueTransactionId);
}

package com.bankingapplication.utils;


import com.bankingapplication.model.request.AccountCreationRequest;
import com.bankingapplication.model.request.TransactionRequest;
import com.bankingapplication.constants.Constants;

public class InputValidator {

    private InputValidator() {
    }

    public static boolean isSearchCriteriaValid(String accountNumber) {
        return Constants.ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).find();
    }

    public static boolean isAccountNoValid(String accountNo) {
        return Constants.ACCOUNT_NUMBER_PATTERN.matcher(accountNo).find();
    }

    public static boolean isCreateAccountCriteriaValid(AccountCreationRequest createAccountInput) {
        return (!(createAccountInput.getName().isBlank() || createAccountInput.getBalance()<0));
    }

    public static boolean isSearchTransactionValid(TransactionRequest transactionInput) {

        if (!isSearchCriteriaValid(transactionInput.getSourceAccount()))
            return false;

        if (!isSearchCriteriaValid(transactionInput.getTargetAccount()))
            return false;

        if (transactionInput.getSourceAccount().equals(transactionInput.getTargetAccount()))
            return false;

        return true;
    }
}

package com.transaction.bankingapplication.controller;

import com.transaction.bankingapplication.model.request.DepositRequest;
import com.transaction.bankingapplication.model.request.WithDrawRequest;
import com.transaction.bankingapplication.model.response.TransactionResponse;
import com.transaction.bankingapplication.model.request.TransactionRequest;
import com.transaction.bankingapplication.services.base.TransactionService;
import com.transaction.bankingapplication.utils.GenericUtils;
import com.transaction.bankingapplication.utils.InputValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.transaction.bankingapplication.constants.Constants.INVALID_SEARCH_CRITERIA;
import static com.transaction.bankingapplication.constants.Constants.INVALID_TRANSACTION;



@Api(value = "Transactions Actions API")
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;


    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);


    @ApiOperation(value = "Transferring amount for user")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200 , message = "Success message for performing transfer request"),
                    @ApiResponse(code = 401 , message = "Unauthorized user for accessing resources"),
                    @ApiResponse(code = 403 , message = "User authentication required"),
                    @ApiResponse(code = 404, message = "Service unavailable")

            }
    )
    @PostMapping(value = "/transfer",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> makeTransfer(
            @Valid @RequestBody TransactionRequest transactionRequest) {

        String uniqueTransactionId = GenericUtils.transactionReferenceNumber();
        LOGGER.info("calling transaction/transfer: {}", uniqueTransactionId);

        if (InputValidator.isSearchTransactionValid(transactionRequest)) {

            TransactionResponse response = transactionService.makeTransfer(transactionRequest, uniqueTransactionId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(INVALID_TRANSACTION, HttpStatus.BAD_REQUEST);
        }
    }



    @ApiOperation(value = "Depositing amount for user")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200 , message = "Success message for performing depositing request"),
                    @ApiResponse(code = 401 , message = "Unauthorized user for accessing resources"),
                    @ApiResponse(code = 403 , message = "User authentication required"),
                    @ApiResponse(code = 404, message = "Service unavailable")

            }
    )
    @PostMapping(value = "/deposit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deposit(
            @Valid @RequestBody DepositRequest depositRequest) {
        LOGGER.debug("Triggered AccountRestController.deposit");

        // Validate input
        if (InputValidator.isAccountNoValid(depositRequest.getTargetAccountNo())) {
            // Attempt to retrieve the account information
            // Return the account details, or warn that no account was found for given input
            String uniqueTransactionId = GenericUtils.transactionReferenceNumber();
            LOGGER.info("calling transaction/deposit: {}", uniqueTransactionId);
            TransactionResponse response = transactionService.makeDeposit(depositRequest,uniqueTransactionId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
        }
    }



    @ApiOperation(value = "With draw amount for user")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200 , message = "Success message for performing withdraw request"),
                    @ApiResponse(code = 401 , message = "Unauthorized user for accessing resources"),
                    @ApiResponse(code = 403 , message = "User authentication required"),
                    @ApiResponse(code = 404, message = "Service unavailable")

            }
    )
    @PostMapping(value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deposit(
            @Valid @RequestBody WithDrawRequest withDrawRequest) {
        LOGGER.debug("Triggered AccountRestController.deposit");

        // Validate input
        if (InputValidator.isAccountNoValid(withDrawRequest.getTargetAccountNo())) {
            // Attempt to retrieve the account information
            // Return the account details, or warn that no account was found for given input
            String uniqueTransactionId = GenericUtils.transactionReferenceNumber();
            LOGGER.info("calling transaction/withdraw: {}", uniqueTransactionId);
            TransactionResponse response = transactionService.makeWithDraw(withDrawRequest,uniqueTransactionId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
        }
    }
}

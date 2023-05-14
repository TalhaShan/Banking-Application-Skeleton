package com.bankingapplication.controller;

import com.bankingapplication.model.entity.Account;
import com.bankingapplication.model.request.AccountCreationRequest;
import com.bankingapplication.services.base.AccountService;
import com.bankingapplication.constants.Constants;
import com.bankingapplication.model.response.AccountStatusResponse;
import com.bankingapplication.utils.InputValidator;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1")
@Api(value = "Account Actions API")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @ApiOperation(value = "Creating Account for new User")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200 , message = "Success message for performing account creation"),
                    @ApiResponse(code = 401 , message = "Unauthorized user for accessing resources"),
                    @ApiResponse(code = 403 , message = "User authentication required"),
                    @ApiResponse(code = 404, message = "Account creation unable")

            }
    )
    @PostMapping(value = "/accounts",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(
            @Valid @RequestBody AccountCreationRequest accountCreationRequest) {
        LOGGER.debug("Triggered AccountRestController.createAccount");

        // Validate input
        if (InputValidator.isCreateAccountCriteriaValid(accountCreationRequest)) {
            // Attempt to retrieve the account information

            Account account = accountService.createNewAccount(accountCreationRequest);

            // Return the account details, or warn that no account was found for given input
            if (account == null) {
                return new ResponseEntity<>(Constants.CREATE_ACCOUNT_FAILED, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(account, HttpStatus.OK);
            }
        } else {
            LOGGER.debug("Triggered AccountRestController.createAccount.failed");
            return new ResponseEntity<>(Constants.INVALID_ACCOUNT_INFORMATION, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Getting Account Details for  User")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200 , message = "Success message for performing account request"),
                    @ApiResponse(code = 401 , message = "Unauthorized user for accessing resources"),
                    @ApiResponse(code = 403 , message = "User authentication required"),
                    @ApiResponse(code = 404, message = "Service unavailable")

            }
    )
    @GetMapping(value = "/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccountDetails(
            @RequestParam @ApiParam(value = "accountNo") String accountNo) {
        LOGGER.debug("Triggered AccountRestController.accountDetails");

        // Validate input
        if (accountNo!=null && !accountNo.isBlank()) {
            // Attempt to retrieve the account information

            AccountStatusResponse accountStatusResponse = accountService.checkAccountBalance(accountNo);
            // Return the account details, or warn that no account was found for given input
                return new ResponseEntity<>(accountStatusResponse, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(Constants.INVALID_ACCOUNT_INFORMATION, HttpStatus.BAD_REQUEST);
        }
    }

}

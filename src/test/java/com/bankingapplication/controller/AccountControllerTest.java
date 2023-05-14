package com.bankingapplication.controller;

import com.bankingapplication.model.request.AccountCreationRequest;
import com.bankingapplication.services.base.AccountService;
import com.bankingapplication.model.entity.Account;
import com.bankingapplication.model.response.AccountStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountControllerTest.class)
class AccountControllerTest {

    @InjectMocks
    AccountController accountController;

    private  static final String URL = "/api/v1/accounts";

    private MockMvc mockMvc;

    @Mock
    AccountService accountService;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }


    @Test
    void givenMissingInput_whenCreatingAccount_thenVerifyBadRequest() throws Exception {
        //POST: localhost:8080/api/v1/accounts
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
     void test_accountCreation_withValidInput_thenVerifySuccess() throws Exception {
        //mock AccountService #createAccount
        Mockito.when(accountService.createNewAccount(any(AccountCreationRequest.class))).thenReturn(new Account());
        //POST: localhost:8080/api/v1/accounts
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{ \"name\":\"James\", \"balance\": \"200\"}"))
                .andExpect(status().isOk());
    }
    @Test
     void test_accountCreation_withMissingParam_thenVerifyBadRequest() throws Exception {
        //mock AccountService #createAccount
        Mockito.when(accountService.createNewAccount(any(AccountCreationRequest.class))).thenReturn(new Account());
        //POST: localhost:8080/api/v1/accounts
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{ \"name\":\"James\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenMissingInput_whenCheckingAccountBalance_thenVerifyBadRequest() throws Exception {
        //GET: localhost:8080/api/v1/accounts
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenValidInput_whenCheckingAccountBalance_thenVerifySuccess() throws Exception {
        //GET: localhost:8080/api/v1/accounts
        Mockito.when(accountService.checkAccountBalance(any(String.class))).thenReturn(new AccountStatusResponse());

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .param("accountNo", "4b17fff5-48e5-4ea4-92a5-46408edfd75e"))
                        .andExpect(status().isOk());
    }
    @Test
    void givenInValidParam_whenCheckingAccountBalance_thenVerifyBadRequest() throws Exception {
        //GET: localhost:8080/api/v1/accounts
        Mockito.when(accountService.checkAccountBalance(any(String.class))).thenReturn(new AccountStatusResponse());

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .param("accountMiss", "4b17fff5-48e5-4ea4-92a5-46408edfd75e"))
                        .andExpect(status().isBadRequest());
    }


}
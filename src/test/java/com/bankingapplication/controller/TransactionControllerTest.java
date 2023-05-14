package com.bankingapplication.controller;

import com.bankingapplication.model.request.DepositRequest;
import com.bankingapplication.model.request.TransactionRequest;
import com.bankingapplication.model.request.WithDrawRequest;
import com.bankingapplication.model.response.TransactionResponse;
import com.bankingapplication.services.base.TransactionService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Disabled
@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionControllerTest.class)
class TransactionControllerTest {

    private  static final String URL = "/api/v1/transactions";
    private  static final String TRANSACTION_REF="168404537782258285012";
    private  static  final String DEPOSIT ="/deposit";
    private  static  final String TRANSFER ="/transfer";
    private  static  final String WITHDRAW ="/withdraw";
    private MockMvc mockMvc;

    @Mock
    TransactionService transactionService;

    @InjectMocks
    TransactionController transactionController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }



    @Test
    void givenMissingInput_whenDepositing_thenVerifyBadRequest() throws Exception {
        //POST: localhost:8080/api/v1/transactions/deposit
        mockMvc.perform(MockMvcRequestBuilders.post(URL+DEPOSIT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void giving_ValidParam_whenWithdrawing_thenVerifySuccess() throws Exception {
        // mock TransactionService #makeWithdraw
        Mockito.when(transactionService.makeWithDraw(any(WithDrawRequest.class), any(String.class)))
                .thenReturn(new TransactionResponse());

        mockMvc.perform(MockMvcRequestBuilders.post(URL+WITHDRAW)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"sourceAccountNo\":\"4b17fff5-48e5-4ea4-92a5-46408edfd75e\",\"amount\":\"10\",\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void givenInValidParam_whenDepositing_thenVerifyBadRequest() throws Exception {
        //POST: localhost:8080/api/v1/transactions/deposit
        //mock TransactionService #makeDeposit
        Mockito.when(transactionService.makeDeposit(any(DepositRequest.class), any(String.class)));

        mockMvc.perform(MockMvcRequestBuilders.post(URL+DEPOSIT)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{ \"name\":\"James\"}"))
                       .andExpect(status().isBadRequest());
    }



    @Test
    void giving_InValidParam_whenWithDrawing_thenVerifyBadRequest() throws Exception {
        //POST: localhost:8080/api/v1/transactions/withdraw
        //mock TransactionService #makeWithdraw
        Mockito.doReturn(transactionService.makeWithDraw(any(WithDrawRequest.class), eq(TRANSACTION_REF)));

        mockMvc.perform(MockMvcRequestBuilders.post(URL+WITHDRAW)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{ \"name\":\"James\"}"))
                .andExpect(status().isBadRequest());
    }
    @SneakyThrows
    @Test
    void giving_ValidParam_whenWithDrawing_thenVerifySuccess()  {
        //POST: localhost:8080/api/v1/transactions/withdraw
     //   mock TransactionService #makeWithdraw
        Mockito.when(transactionService.makeWithDraw(any(WithDrawRequest.class), any(String.class)))
                .thenReturn(new TransactionResponse());

        mockMvc.perform(MockMvcRequestBuilders.post(URL+WITHDRAW)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"targetAccountNo\":\"4b17fff5-48e5-4ea4-92a5-46408edfd75e\",\"amount\":\"20\",\"currency\":\"USD\"}"))
                .andExpect(status().isOk());
    }


    @Test
    void givenMissingInput_whenTransfer_thenVerifyBadRequest() throws Exception {
        //POST: localhost:8080/api/v1/transactions/transfer
        mockMvc.perform(MockMvcRequestBuilders.post(URL+TRANSFER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void giving_ValidParam_whenTransfer_thenVerifySuccess(){
        //POST: localhost:8080/api/v1/transactions/transfer
        //mock TransactionService #makeTransfer
        Mockito.when(transactionService.makeTransfer(any(TransactionRequest.class), eq(TRANSACTION_REF)))
                .thenReturn(new TransactionResponse());

        mockMvc.perform(MockMvcRequestBuilders.post(URL+TRANSFER)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"targetAccount\":\"f0233c29-d96d-4c73-9b98-1bb55d77d5ad\",\"sourceAccount\":\"4b17fff5-48e5-4ea4-92a5-46408edfd75e\",\"amount\":\"10\",\"currency\":\"AED\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void giving_InValidParamValue_forCurrency_thenVerifyBadRequest() throws Exception {
        //POST: localhost:8080/api/v1/transactions/transfer
        //mock TransactionService #makeTransfer
        Mockito.when(transactionService.makeTransfer(any(TransactionRequest.class),any(String.class)))
                .thenReturn(new TransactionResponse());// complete the stub

        mockMvc.perform(MockMvcRequestBuilders.post(URL+TRANSFER)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"targetAccount\":\"f0233c29-d96d-4c73-9b98-1bb55d77d5ad\",\"sourceAccount\":\"4b17fff5-48e5-4ea4-92a5-46408edfd75e\",\"amount\":\"10\",\"currency\":\"AEDDD\"}"))
                .andExpect(status().isBadRequest());
    }



    @AfterEach
    void tearDown(){

    }

}
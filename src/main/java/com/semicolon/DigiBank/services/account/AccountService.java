package com.semicolon.DigiBank.services.account;

import com.semicolon.DigiBank.data.models.Account;
import com.semicolon.DigiBank.data.models.Transaction;
import com.semicolon.DigiBank.dtos.requests.*;
import com.semicolon.DigiBank.dtos.responses.AccountInfoResponse;
import com.semicolon.DigiBank.dtos.responses.AccountApiResponse;
import com.semicolon.DigiBank.web.exceptions.*;


import java.util.List;

public interface AccountService {
    AccountApiResponse createAccount(CreateAccountRequest createAccountRequest) throws DigiBankException, AccountNameAlreadyExistsException;
    AccountInfoResponse checkAccountInfo(String accountNumber) throws DigiBankException, AccountNotFoundException;

    AccountApiResponse makeDeposit(DepositRequest depositRequest) throws DigiBankException, UnsupportedDepositException, AccountNotFoundException;
    AccountApiResponse withdraw(WithdrawalRequest withdrawalRequest) throws DigiBankException, UnsupportedWithdrawalException, AccountNotFoundException, InvalidDetailsException;

    Account findAccountByName(String accountName);

    List<Transaction> getAccountStatement(String accountNumber) throws AccountNotFoundException;

    AccountApiResponse transferFund(TransferRequest request) throws AccountNotFoundException, InvalidDetailsException, UnsupportedWithdrawalException, UnsupportedDepositException;

    String deleteAccount(DeleteAccountRequest request) throws AccountNotFoundException, InvalidDetailsException;
}
package com.semicolon.DigiBank.services.user;


import com.semicolon.DigiBank.data.models.Transaction;
import com.semicolon.DigiBank.dtos.requests.*;
import com.semicolon.DigiBank.dtos.responses.AccountApiResponse;
import com.semicolon.DigiBank.dtos.responses.AccountInfoResponse;
import com.semicolon.DigiBank.dtos.responses.LogInResponse;
import com.semicolon.DigiBank.dtos.responses.SignUpResponse;
import com.semicolon.DigiBank.web.exceptions.*;

import java.util.List;

public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest) throws DigiBankException;
    LogInResponse logIn(LogInRequest logInRequest) throws AccountNotFoundException;
    AccountInfoResponse getAccountInfo(String accountNumber) throws DigiBankException, AccountNotFoundException;
    List<Transaction> fetchAccountStatement(String accountNumber) throws AccountNotFoundException;

    AccountApiResponse makeDeposit(DepositRequest request) throws UnsupportedDepositException, DigiBankException, AccountNotFoundException;
    AccountApiResponse transferFund(TransferRequest request) throws UnsupportedDepositException, InvalidDetailsException, UnsupportedWithdrawalException, AccountNotFoundException;
    String closeAccount(DeleteAccountRequest request) throws InvalidDetailsException, AccountNotFoundException, UserDoesNotExistException;

    AccountApiResponse withdraw(WithdrawalRequest request) throws InvalidDetailsException, UnsupportedWithdrawalException, AccountNotFoundException, DigiBankException;
}

package com.semicolon.DigiBank.services.user;

import com.semicolon.DigiBank.data.models.Account;
import com.semicolon.DigiBank.data.models.Transaction;
import com.semicolon.DigiBank.data.models.User;
import com.semicolon.DigiBank.data.repositories.user.UserRepository;
import com.semicolon.DigiBank.dtos.requests.*;
import com.semicolon.DigiBank.dtos.responses.AccountApiResponse;
import com.semicolon.DigiBank.dtos.responses.AccountInfoResponse;
import com.semicolon.DigiBank.dtos.responses.LogInResponse;
import com.semicolon.DigiBank.dtos.responses.SignUpResponse;
import com.semicolon.DigiBank.services.account.AccountService;
import com.semicolon.DigiBank.services.bank.BankService;
import com.semicolon.DigiBank.web.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private AccountService accountService;
    private BankService bankService;


    public UserServiceImpl(UserRepository userRepository, AccountService accountService, BankService bankService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.bankService = bankService;
    }


    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws DigiBankException {
        String accountName = generateAccountName(signUpRequest.getFirstName(),  signUpRequest.getLastName());
        checkIfAccountNameExists(accountName);

        User user = buildUser(signUpRequest);
        CreateAccountRequest createAccountRequest = buildCreateAccountRequest(signUpRequest, accountName);

        AccountApiResponse accountApiResponse = accountService.createAccount(createAccountRequest);

        user.setAccount(accountApiResponse.getAccount());
        User savedUser = userRepository.saveUser(user);
        User bankUser = bankService.saveCustomer(savedUser);
        return SignUpResponse.builder()
                .accountNumber(bankUser.getAccount().getAccountNumber())
                .message("Account Creation Success")
                .statusCode(200)
                .successful(true)
                .build();
    }

    @Override
    public LogInResponse logIn(LogInRequest logInRequest) throws AccountNotFoundException {
        Account account = accountService.findAccountByName(logInRequest.getAccountNumber());
        if(account == null){
            throw new AccountNotFoundException("No Such Account Exists");
        }
        return LogInResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public AccountInfoResponse getAccountInfo(String accountNumber) throws DigiBankException, AccountNotFoundException {
        return accountService.checkAccountInfo(accountNumber);
    }

    @Override
    public List<Transaction> fetchAccountStatement(String accountNumber) throws AccountNotFoundException {
        return accountService.getAccountStatement(accountNumber);
    }

    @Override
    public AccountApiResponse makeDeposit(DepositRequest request) throws UnsupportedDepositException, DigiBankException, AccountNotFoundException {
        return accountService.makeDeposit(request);
    }

    @Override
    public AccountApiResponse transferFund(TransferRequest request) throws UnsupportedDepositException, InvalidDetailsException, UnsupportedWithdrawalException, AccountNotFoundException {

        return accountService.transferFund(request);
    }

    @Override
    public String closeAccount(DeleteAccountRequest request) throws InvalidDetailsException, AccountNotFoundException, UserDoesNotExistException {
        return bankService.deleteUser(request);
    }


    private CreateAccountRequest buildCreateAccountRequest(SignUpRequest signUpRequest, String accountName) {
        return CreateAccountRequest.builder()
                .accountName(accountName)
                .pin(signUpRequest.getPassword())
                .initialDeposit(signUpRequest.getInitialDeposit())
                .build();
    }



    private User buildUser(SignUpRequest signUpRequest) {
        return User.builder()
                .lastName(signUpRequest.getLastName())
                .firstName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .build();
    }

    private void checkIfAccountNameExists(String accountName) throws AccountNameAlreadyExistsException {
        Account account = accountService.findAccountByName(accountName);
        if(account != null){ throw new AccountNameAlreadyExistsException("Name already exist"); }
    }

    private String generateAccountName(String firstName, String lastName) {
        return firstName + " "+ lastName;
    }
}

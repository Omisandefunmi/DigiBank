package com.semicolon.DigiBank.services.user;

import com.semicolon.DigiBank.data.models.Account;
import com.semicolon.DigiBank.data.models.Role;
import com.semicolon.DigiBank.data.models.Transaction;
import com.semicolon.DigiBank.data.models.User;
import com.semicolon.DigiBank.data.models.enums.RoleType;
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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final BankService bankService;


//    public UserServiceImpl(UserRepository userRepository, AccountService accountService, BankService bankService) {
//        this.userRepository = userRepository;
//        this.accountService = accountService;
//        this.bankService = bankService;
//    }


    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws DigiBankException {
        String accountName = generateAccountName(signUpRequest.getFirstName(),  signUpRequest.getLastName());
        checkIfAccountNameExists(accountName);
        log.info("requests details have been verified. Proceed to creating user");

        User user = buildUser(signUpRequest);
        log.info("user has been created. Proceed to initializing role set");
        Set<Role> userRoles = user.getRoles();
//        log.info("role size is "+userRoles.size());
        CreateAccountRequest createAccountRequest = buildCreateAccountRequest(signUpRequest, accountName);

        AccountApiResponse accountApiResponse = accountService.createAccount(createAccountRequest);
        log.info("account number is "+accountApiResponse.getAccount().getAccountNumber());

        user.setAccount(accountApiResponse.getAccount());
        if(userRoles == null){
            userRoles= new HashSet<>();
            userRoles.add(new Role(RoleType.USER));
            user.setRoles(userRoles);
            log.info("role size is "+user.getRoles().size());
        }
        User savedUser = userRepository.saveUser(user);
        log.info("User has been saved in the user repo");
        User bankUser = bankService.saveCustomer(savedUser);
        log.info("User has been saved in the bank repo");
        log.info("account number is "+accountApiResponse.getAccount().getAccountNumber());
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

    @Override
    public AccountApiResponse withdraw(WithdrawalRequest request) throws InvalidDetailsException, UnsupportedWithdrawalException, AccountNotFoundException, DigiBankException {
        return accountService.withdraw(request);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByAccountName(username);
        if(user != null){
            return new org.springframework.security.core.userdetails.User(user.getAccount().getAccountNumber(),
                    user.getPassword(), getAuthorities(user.getRoles()));
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles){
        return roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getRoleType().name())
        ).collect(Collectors.toSet());
    }
}

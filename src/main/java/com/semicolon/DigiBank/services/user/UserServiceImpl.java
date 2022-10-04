package com.semicolon.DigiBank.services.user;

import com.semicolon.DigiBank.data.models.Account;
import com.semicolon.DigiBank.data.models.Transaction;
import com.semicolon.DigiBank.data.models.User;
import com.semicolon.DigiBank.data.models.enums.RoleType;
import com.semicolon.DigiBank.data.repositories.user.UserRepository;
import com.semicolon.DigiBank.dtos.requests.*;
import com.semicolon.DigiBank.dtos.responses.AccountApiResponse;
import com.semicolon.DigiBank.dtos.responses.AccountInfoResponse;
import com.semicolon.DigiBank.dtos.responses.SignUpResponse;
import com.semicolon.DigiBank.services.account.AccountService;
import com.semicolon.DigiBank.services.bank.BankService;
import com.semicolon.DigiBank.web.exceptions.*;
import lombok.RequiredArgsConstructor;
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
    private static int idCounter;
    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws DigiBankException, AccountNameAlreadyExistsException {
        String accountName = generateAccountName(signUpRequest.getFirstName(),  signUpRequest.getLastName());
        checkIfAccountNameExists(accountName);
        log.info("requests details have been verified. Proceed to creating user");

        CreateAccountRequest createAccountRequest = buildCreateAccountRequest(signUpRequest, accountName);

        accountService.createAccount(createAccountRequest);


        Account account = accountService.findAccountByName(accountName);
        log.info("account number is "+account.getAccountNumber());
        User user = buildUser(signUpRequest);
        log.info("user has been created. Proceed to initializing role set");

        user.setAccount(account);
        user.setId(String.valueOf(++idCounter));
        assignDefaultRoleTypeTo(user);
        userRepository.saveUser(user);
        log.info("User has been saved in the user repo");
        User bankUser = bankService.saveCustomer(user);
        log.info("User has been saved in the bank repo");
        log.info("account number is "+user.getAccount().getAccountNumber());
        return SignUpResponse.builder()
                .accountNumber(bankUser.getAccount().getAccountNumber())
                .message("Account Creation Success")
                .statusCode(200)
                .successful(true)
                .build();
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

    private void assignDefaultRoleTypeTo(User user) {
        Set<RoleType> userRoles = user.getRoles();
        if(userRoles == null){
            userRoles= new HashSet<>();
            userRoles.add(RoleType.USER);
            user.setRoles(userRoles);
            log.info("role size is "+ user.getRoles().size());
        }
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



    private Collection<? extends GrantedAuthority> getAuthorities(Set<RoleType> roles){
        return roles.stream().map(
                role -> new SimpleGrantedAuthority(role.name())
        ).collect(Collectors.toSet());
    }
}

package com.semicolon.DigiBank.web.controllers.noAuthControllers;

import com.semicolon.DigiBank.dtos.requests.*;
import com.semicolon.DigiBank.services.user.UserService;
import com.semicolon.DigiBank.web.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create_account")
    public ResponseEntity<?> openAccount(@RequestBody SignUpRequest signUpRequest){
        try{
            var apiResponse = userService.signUp(signUpRequest);
            return  new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (DigiBankException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/account_info/{accountNumber}")
    public ResponseEntity<?> accountInfo(@PathVariable("accountNumber") String accountNumber) {
        try{
            var apiResponse = userService.getAccountInfo(accountNumber);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DigiBankException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/account_statement/{accountNumber}")
    public ResponseEntity<?> generateAccountStatement(@PathVariable("accountNumber") String accountNumber){
        try{
            var apiResponse = userService.fetchAccountStatement(accountNumber);
            return new ResponseEntity<>(apiResponse, HttpStatus.FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request){
        try {
            var apiResponse = userService.makeDeposit(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (UnsupportedDepositException | DigiBankException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawalRequest request) {
        try {
            var apiResponse = userService.withdraw(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
        }
        catch (AccountNotFoundException | DigiBankException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidDetailsException | UnsupportedWithdrawalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        try {
            var apiResponse = userService.transferFund(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (UnsupportedDepositException | UnsupportedWithdrawalException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InvalidDetailsException | AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/close_account")
    public ResponseEntity<?> closeAccount(@RequestBody DeleteAccountRequest request) {
        try {
            var apiResponse = userService.closeAccount(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
        } catch (InvalidDetailsException | UserDoesNotExistException | AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

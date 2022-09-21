package com.semicolon.DigiBank.web.controllers.noAuthControllers;

import com.semicolon.DigiBank.dtos.requests.*;
import com.semicolon.DigiBank.services.account.AccountService;
import com.semicolon.DigiBank.web.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/account_info/{accountNumber}")
    public ResponseEntity<?> accountInfo(@PathVariable("accountNumber") String accountNumber) {
        try{
            var apiResponse = accountService.checkAccountInfo(accountNumber);
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
            var apiResponse = accountService.getAccountStatement(accountNumber);
            return new ResponseEntity<>(apiResponse, HttpStatus.FOUND);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request){
        try {
            var apiResponse = accountService.makeDeposit(request);
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
            var apiResponse = accountService.withdraw(request);
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
            var apiResponse = accountService.transferFund(request);
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
            var apiResponse = accountService.deleteAccount(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
        } catch (InvalidDetailsException | AccountNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{accountName}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> findAccountByAccountName(@PathVariable("accountName") String accountName){
        var response = accountService.findAccountByName(accountName);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

}

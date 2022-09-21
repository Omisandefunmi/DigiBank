package com.semicolon.DigiBank.web.controllers.noAuthControllers;

import com.semicolon.DigiBank.dtos.requests.*;
import com.semicolon.DigiBank.services.account.AccountService;
import com.semicolon.DigiBank.web.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/account_info/{accountNumber}")
    public ResponseEntity<?> accountInfo(@PathVariable("accountNumber") String accountNumber) throws AccountNotFoundException, DigiBankException {
            var apiResponse = accountService.checkAccountInfo(accountNumber);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @GetMapping("/account_statement/{accountNumber}")
    public ResponseEntity<?> generateAccountStatement(@PathVariable("accountNumber") String accountNumber) throws AccountNotFoundException {
            var apiResponse = accountService.getAccountStatement(accountNumber);
            return new ResponseEntity<>(apiResponse, HttpStatus.FOUND);

    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody DepositRequest request) throws UnsupportedDepositException, AccountNotFoundException, DigiBankException {
            var apiResponse = accountService.makeDeposit(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @PostMapping("/withdrawal")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawalRequest request) throws InvalidDetailsException, UnsupportedWithdrawalException, AccountNotFoundException, DigiBankException {
            var apiResponse = accountService.withdraw(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) throws UnsupportedDepositException, InvalidDetailsException, UnsupportedWithdrawalException, AccountNotFoundException {
            var apiResponse = accountService.transferFund(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/close_account")
    public ResponseEntity<?> closeAccount(@RequestBody DeleteAccountRequest request) throws InvalidDetailsException, AccountNotFoundException {
            var apiResponse = accountService.deleteAccount(request);
            return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{accountName}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> findAccountByAccountName(@PathVariable("accountName") String accountName){
        var response = accountService.findAccountByName(accountName);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

}

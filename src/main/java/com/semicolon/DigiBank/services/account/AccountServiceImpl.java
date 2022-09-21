package com.semicolon.DigiBank.services.account;

import com.semicolon.DigiBank.data.models.Account;
import com.semicolon.DigiBank.data.models.Transaction;
import com.semicolon.DigiBank.data.models.enums.TransactionType;
import com.semicolon.DigiBank.data.repositories.account.AccountRepository;
import com.semicolon.DigiBank.dtos.requests.*;
import com.semicolon.DigiBank.dtos.responses.AccountInfoResponse;
import com.semicolon.DigiBank.dtos.responses.AccountApiResponse;
import com.semicolon.DigiBank.web.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;


//    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper) {
//        this.accountRepository = accountRepository;
//        this.modelMapper = modelMapper;
//    }

    @Override
    public AccountApiResponse createAccount(CreateAccountRequest createAccountRequest) throws DigiBankException, AccountNameAlreadyExistsException {
        Account account = accountRepository.findAccountByName(createAccountRequest.getAccountName());
        if(account != null){
            throw new AccountNameAlreadyExistsException(createAccountRequest.getAccountName()+" already exists");
        }
        if(createAccountRequest.getInitialDeposit() < 500){
            throw new DigiBankException("Initial deposit can not be less than "+500);
        }
        if(createAccountRequest.getPin().length() != 4){
            throw new DigiBankException("Account pin cannot be greater or lesser than 4");
        }

         account = Account.builder()
                .name(createAccountRequest.getAccountName())
                .pin(createAccountRequest.getPin())
                .build();

        String accountNumber = generateAccountNumber();
        account.setAccountNumber(accountNumber);

        Account saved = accountRepository.save(account);
        saved.setId(accountRepository.size()+"");
        return AccountApiResponse.builder()
                .statusCode(200)
                .message("Account creation successful with account number "+ saved.getAccountNumber())
                .success(true)
                .build();
    }

    private String generateAccountNumber() {
        String generated = UUID.randomUUID().toString();
        return generated.substring(0, 9);
    }

    @Override
    public AccountInfoResponse checkAccountInfo(String accountNumber) throws AccountNotFoundException {
       Account account = accountRepository.findAccountByAccountNumber(accountNumber);
      verifyThatAccountExists(account);
        return AccountInfoResponse.builder()
                .accountName(account.getName())
                .accountNumber(account.getAccountNumber())
                .accountBalance(account.getBalance().doubleValue())
                .build();
    }

    @Override
    public AccountApiResponse makeDeposit(DepositRequest depositRequest) throws UnsupportedDepositException, AccountNotFoundException {
        Account depositAccount = accountRepository.findAccountByAccountNumber(depositRequest.getAccountNumber());
       verifyThatAccountExists(depositAccount);
        if(!(depositRequest.getAmount() >= 1)){
            throw new UnsupportedDepositException("Error!!! Amount too low!");
        }

        if(!( depositRequest.getAmount() <= 1_000_000)){
            throw new UnsupportedDepositException("Error!!! Amount too high");
        }
        depositAccount.setBalance(BigDecimal.valueOf(depositAccount.getBalance().doubleValue() + depositRequest.getAmount()));
        String transactionDate = generateTransactionDateAndTime();
        Transaction transaction = Transaction.builder()
                .id((depositAccount.getTransactionList().size() + 1 )+"")
                .amount(BigDecimal.valueOf(depositRequest.getAmount()))
                .transactionType(TransactionType.CREDIT)
                .narration(depositRequest.getNarration())
                .remainingBalance(depositAccount.getBalance())
                .transactDate(transactionDate)
                .build();

        depositAccount.getTransactionList().add(transaction);
        return AccountApiResponse.builder()
                .message("Deposit success")
                .statusCode(200)
                .success(true)
                .build();
    }

    private String generateTransactionDateAndTime() {
        LocalDateTime transactionTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy,  hh:mm:ss");
        return formatter.format(transactionTime);
    }

    @Override
    public AccountApiResponse withdraw(WithdrawalRequest withdrawalRequest) throws UnsupportedWithdrawalException, AccountNotFoundException, InvalidDetailsException {
        Account account = accountRepository.findAccountByAccountNumber(withdrawalRequest.getAccountNumber());
        verifyThatAccountExists(account);
       verifyPassword(withdrawalRequest.getPin(), account.getPin());

        ifWithdrawAble(account.getBalance(), withdrawalRequest.getAmount());


        double newBalance = account.getBalance().doubleValue() - withdrawalRequest.getAmount();
        account.setBalance(BigDecimal.valueOf(newBalance));
        String transactionDate = generateTransactionDateAndTime();
        Transaction transaction = Transaction.builder()
                .id((account.getTransactionList().size() + 1 )+"")
                .amount(BigDecimal.valueOf(withdrawalRequest.getAmount()))
                .transactionType(TransactionType.DEPOSIT)
                .narration(withdrawalRequest.getNarration())
                .remainingBalance(BigDecimal.valueOf(newBalance))
                .transactDate(transactionDate)
                .build();

        account.getTransactionList().add(transaction);

        return AccountApiResponse.builder()
                .success(true)
                .statusCode(200)
                .message("Withdraw success")
                .build();
    }


    @Override
    public Account findAccountByName(String accountName) {
        return accountRepository.findAccountByName(accountName);
    }

    @Override
    public List<Transaction> getAccountStatement(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
       verifyThatAccountExists(account);
        return account.getTransactionList();

    }

    @Override
    public AccountApiResponse transferFund(TransferRequest request) throws AccountNotFoundException, InvalidDetailsException, UnsupportedWithdrawalException, UnsupportedDepositException {
        Account senderAccount = accountRepository.findAccountByAccountNumber(request.getSenderAccountNumber());
        verifyThatAccountExists(senderAccount);

        Account receiverAccount = accountRepository.findAccountByAccountNumber(request.getReceiverAccountNumber());
        verifyThatAccountExists(receiverAccount);

        verifyPassword(request.getPassword(), senderAccount.getPin());

        ifWithdrawAble(senderAccount.getBalance(), request.getAmount());

        WithdrawalRequest withdrawalRequest = buildWithdrawRequest(request, senderAccount);

        AccountApiResponse response = withdraw(withdrawalRequest);

        DepositRequest depositRequest = buildDepositRequest(request);

        makeDeposit(depositRequest);

        return response;
    }

    @Override
    public String deleteAccount(DeleteAccountRequest request) throws AccountNotFoundException, InvalidDetailsException {
        Account account = accountRepository.findAccountByAccountNumber(request.getAccountNumber());
        verifyThatAccountExists(account);
        verifyPassword(request.getPin(), account.getPin());
        verifyAccountName(request, account);
        Account deletedAccount = accountRepository.deleteAccount(account.getAccountNumber());
        return deletedAccount.getAccountNumber()+" has been deleted";
    }

    private void verifyAccountName(DeleteAccountRequest request, Account account) throws InvalidDetailsException {
        if(account.getAccountNumber().equals((request.getFirstName()+" "+ request.getLastName()))){
            throw new InvalidDetailsException("Details Mismatch");
        }
    }

    private DepositRequest buildDepositRequest(TransferRequest request) {
        return DepositRequest.builder()
                .accountNumber(request.getReceiverAccountNumber())
                .amount(request.getAmount())
                .narration(request.getNarration())
                .build();
    }

    private WithdrawalRequest buildWithdrawRequest(TransferRequest request, Account senderAccount) {
        return WithdrawalRequest.builder()
                .accountNumber(senderAccount.getAccountNumber())
                .amount(request.getAmount())
                .pin(request.getPassword())
                .narration(request.getNarration())
                .build();
    }

    private void verifyPassword(String requestPassword, String actualPassword) throws InvalidDetailsException {
        if(!(actualPassword.equals(requestPassword))){
            throw new InvalidDetailsException("Invalid account details");
        }
    }

    private void ifWithdrawAble(BigDecimal balance, double amount) throws UnsupportedWithdrawalException {
        if(!(balance.doubleValue() - amount >= 500)){
            throw new UnsupportedWithdrawalException("Insufficient balance");
        }
    }

    private void verifyThatAccountExists(Account account) throws AccountNotFoundException {
        if(account == null){
            throw new AccountNotFoundException("Account Not Found");
        }
    }

}

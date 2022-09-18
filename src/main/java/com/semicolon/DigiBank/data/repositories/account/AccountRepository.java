package com.semicolon.DigiBank.data.repositories.account;

import com.semicolon.DigiBank.data.models.Account;


public interface AccountRepository {

    Account findAccountByName(String accountName);

    int size();

    Account save(Account account);

   Account findAccountByAccountNumber(String accountNumber);

    Account deleteAccount(String accountNumber);
}

package com.semicolon.DigiBank.data.repositories.account;

import com.semicolon.DigiBank.data.models.Account;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final Map<String, Account> accountRepo = new HashMap<>();

    @Override
    public Account findAccountByName(String accountName) {
        for (Map.Entry<String, Account> accountEntry : accountRepo.entrySet()) {
            if(accountEntry.getValue().getName().equalsIgnoreCase(accountName)){
                return accountEntry.getValue();
            }
        }
        return null;
    }

    @Override
    public int size() {
        return accountRepo.size();
    }

    @Override
    public Account save(Account account) {
        return accountRepo.put(account.getAccountNumber(), account);

    }

    @Override
    public Account findAccountByAccountNumber(String accountNumber) {
        return accountRepo.get(accountNumber);
    }

    @Override
    public Account deleteAccount(String accountNumber) {
        return accountRepo.remove(accountNumber);
    }
}

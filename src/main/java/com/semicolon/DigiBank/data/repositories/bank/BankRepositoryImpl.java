package com.semicolon.DigiBank.data.repositories.bank;

import com.semicolon.DigiBank.data.models.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
@Repository
public class BankRepositoryImpl implements BankRepository{
    private Map<String, User> bankUsers = new HashMap<>();

    @Override
    public User saveCustomer(User savedUser) {
        String accountNumber = generateKey(savedUser);
        return bankUsers.put(accountNumber, savedUser);
    }

    @Override
    public User findUserByAccountNumber(String accountNumber) {
        return bankUsers.get(accountNumber);
    }

    private String generateKey(User savedUser) {
        return savedUser.getAccount().getAccountNumber();
    }
}

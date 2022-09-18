package com.semicolon.DigiBank.data.repositories.bank;

import com.semicolon.DigiBank.data.models.User;

public interface BankRepository {
    User saveCustomer(User savedUser);

    User findUserByAccountNumber(String accountNumber);
}

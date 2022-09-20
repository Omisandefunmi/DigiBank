package com.semicolon.DigiBank.data.repositories.user;

import com.semicolon.DigiBank.data.models.User;

public interface UserRepository {
    User saveUser(User user);
    User findUserByAccountName(String accountNumber);
}

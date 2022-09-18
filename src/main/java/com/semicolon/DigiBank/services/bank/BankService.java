package com.semicolon.DigiBank.services.bank;

import com.semicolon.DigiBank.data.models.User;
import com.semicolon.DigiBank.dtos.requests.DeleteAccountRequest;
import com.semicolon.DigiBank.web.exceptions.AccountNotFoundException;
import com.semicolon.DigiBank.web.exceptions.InvalidDetailsException;
import com.semicolon.DigiBank.web.exceptions.UserDoesNotExistException;

public interface BankService {
    User saveCustomer(User savedUser);
    String deleteUser(DeleteAccountRequest request) throws InvalidDetailsException, AccountNotFoundException, UserDoesNotExistException;
}

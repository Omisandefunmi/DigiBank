package com.semicolon.DigiBank.web.exceptions;

public class AccountNameAlreadyExistsException extends Exception {
    public AccountNameAlreadyExistsException(String message) {
        super(message);
    }
}

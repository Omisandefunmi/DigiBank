package com.semicolon.DigiBank.web.exceptions;

public class AccountNameAlreadyExistsException extends RuntimeException {
    public AccountNameAlreadyExistsException(String message) {
        super(message);
    }
}

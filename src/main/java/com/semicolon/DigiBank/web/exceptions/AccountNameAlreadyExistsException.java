package com.semicolon.DigiBank.web.exceptions;

public class AccountNameAlreadyExistsException extends DigiBankException {
    public AccountNameAlreadyExistsException(String message) {
        super(message);
    }
}

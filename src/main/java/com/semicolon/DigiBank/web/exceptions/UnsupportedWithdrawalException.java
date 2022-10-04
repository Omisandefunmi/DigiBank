package com.semicolon.DigiBank.web.exceptions;

public class UnsupportedWithdrawalException extends RuntimeException {
    public UnsupportedWithdrawalException(String message) {
        super(message);
    }
}

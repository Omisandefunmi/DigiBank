package com.semicolon.DigiBank.web.exceptions;

public class UnsupportedDepositException extends RuntimeException {
    public UnsupportedDepositException(String message) {
        super(message);
    }
}

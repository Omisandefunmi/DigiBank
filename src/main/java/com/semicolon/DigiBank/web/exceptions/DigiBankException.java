package com.semicolon.DigiBank.web.exceptions;

public class DigiBankException extends RuntimeException{

    private int statusCode;

    public DigiBankException(String message){
        super(message);
    }

    public DigiBankException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

package com.semicolon.DigiBank.dtos.requests;

import lombok.Getter;

@Getter
public class LogInRequest {
    private String accountNumber;
    private String pin;
}

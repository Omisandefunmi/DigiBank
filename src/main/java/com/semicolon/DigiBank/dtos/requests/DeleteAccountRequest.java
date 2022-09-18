package com.semicolon.DigiBank.dtos.requests;

import lombok.Getter;

@Getter
public class DeleteAccountRequest {
    private String firstName;
    private String lastName;
    private String accountNumber;
    private String pin;
}

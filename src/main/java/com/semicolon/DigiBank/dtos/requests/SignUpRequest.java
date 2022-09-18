package com.semicolon.DigiBank.dtos.requests;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private double initialDeposit;

}

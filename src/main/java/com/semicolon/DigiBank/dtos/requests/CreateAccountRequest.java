package com.semicolon.DigiBank.dtos.requests;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAccountRequest {
    private String accountName;
    private String pin;
    private double initialDeposit;

}

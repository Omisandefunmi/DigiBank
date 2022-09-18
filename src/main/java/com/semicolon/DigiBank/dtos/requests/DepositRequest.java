package com.semicolon.DigiBank.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DepositRequest {
    private String accountNumber;
    private double amount;
    private String narration;

}

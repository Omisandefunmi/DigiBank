package com.semicolon.DigiBank.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawalRequest {
    private String accountNumber;
    private String pin;
    private double amount;
    private String narration;

}

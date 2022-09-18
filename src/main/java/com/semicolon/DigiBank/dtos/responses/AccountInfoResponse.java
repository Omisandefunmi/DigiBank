package com.semicolon.DigiBank.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfoResponse {
    private String accountName;
    private String accountNumber;
    private double accountBalance;

}

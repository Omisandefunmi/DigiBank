package com.semicolon.DigiBank.dtos.responses;

import com.semicolon.DigiBank.data.models.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AccountApiResponse {
    private boolean success;
    private String message;
    private int statusCode;
    private Account account;
}

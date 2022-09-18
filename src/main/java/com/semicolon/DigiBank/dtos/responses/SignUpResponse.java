package com.semicolon.DigiBank.dtos.responses;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponse {
    private int statusCode;
    private boolean successful;
    private String accountNumber;
    private String message;
}

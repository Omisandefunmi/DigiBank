package com.semicolon.DigiBank.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogInResponse {
    private boolean success;
    private String accessToken;
}

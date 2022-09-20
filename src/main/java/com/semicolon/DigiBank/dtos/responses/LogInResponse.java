package com.semicolon.DigiBank.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class LogInResponse {
    private boolean success;
    private String accessToken;
}

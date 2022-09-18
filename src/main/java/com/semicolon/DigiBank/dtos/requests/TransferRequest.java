package com.semicolon.DigiBank.dtos.requests;

import lombok.Getter;

@Getter
public class TransferRequest {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private double amount;
    private String password;
    private String narration;
}

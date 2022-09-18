package com.semicolon.DigiBank.dtos.responses;

import com.semicolon.DigiBank.data.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountStatementResponse {
    private List<Transaction> transactionList;
}

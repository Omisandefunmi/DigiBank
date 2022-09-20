package com.semicolon.DigiBank.data.models;

import com.semicolon.DigiBank.data.models.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String id;
    private String narration;
    private BigDecimal amount;
    private TransactionType transactionType;
    private BigDecimal remainingBalance;
    private String transactDate;
}

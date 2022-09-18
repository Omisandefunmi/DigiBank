package com.semicolon.DigiBank.data.models;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

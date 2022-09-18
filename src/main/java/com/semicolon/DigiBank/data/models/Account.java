package com.semicolon.DigiBank.data.models;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String id;
    private String name;
    private String accountNumber;
    private BigDecimal minimumBalance;
    private BigDecimal balance;
    private String pin;
    private List<Transaction> transactionList;
}

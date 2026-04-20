package com.example.BudgetTracker.DTO;

import com.example.BudgetTracker.Entity.TransactionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequestDTO {
    private BigDecimal amount;

    private String description;

    private TransactionStatus status;

    private UUID categoryId;
}
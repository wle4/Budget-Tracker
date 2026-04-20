package com.example.BudgetTracker.DTO;

import com.example.BudgetTracker.Entity.Category;
import com.example.BudgetTracker.Entity.Transaction;
import com.example.BudgetTracker.Entity.TransactionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponseDTO {
    private UUID id;
    private UUID accountId;
    private UUID categoryId;
    private BigDecimal amount;
    private String description;
    private TransactionStatus status;
    private UUID reversalOf;
    private LocalDateTime createdAt;
}

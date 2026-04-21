package com.example.BudgetTracker.DTO;

import com.example.BudgetTracker.Entity.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequestDTO {
    @NotBlank(message = "Transaction amount is required")
    private BigDecimal amount;

    @NotBlank(message = "Transaction description is required")
    private String description;

    @NotNull(message = "Transaction status is required")
    private TransactionStatus status;

    private UUID categoryId;
}
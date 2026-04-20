package com.example.BudgetTracker.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AccountResponseDTO {
    private UUID id;

    private String name;

    private String type;

    private BigDecimal balance;

    private LocalDateTime createdAt;

    private UUID userId;
}

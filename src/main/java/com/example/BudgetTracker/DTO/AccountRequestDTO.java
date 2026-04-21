package com.example.BudgetTracker.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {
    @NotBlank(message = "Account name is required")
    private String name;

    @NotBlank(message = "Account type is required")
    private String type;

    @NotNull(message = "Account balance is required")
    private BigDecimal balance;
}

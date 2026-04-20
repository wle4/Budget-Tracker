package com.example.BudgetTracker.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
public class AccountRequestDTO {
    private String name;

    private String type;

    private BigDecimal balance;
}

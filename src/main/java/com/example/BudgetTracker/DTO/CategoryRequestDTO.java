package com.example.BudgetTracker.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryRequestDTO {
    private UUID userId;
    private String name;
}
package com.example.BudgetTracker.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryResponseDTO {
    private UUID id;
    private UUID userId;
    private String name;
}
package com.example.BudgetTracker.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoryRequestDTO {
    @NotBlank(message = "Category name is required")
    private String name;
}
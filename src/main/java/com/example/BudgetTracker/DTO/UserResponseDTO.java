package com.example.BudgetTracker.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponseDTO {
    UUID id;

    String email;

    String fullName;

    private LocalDateTime createdAt;
}
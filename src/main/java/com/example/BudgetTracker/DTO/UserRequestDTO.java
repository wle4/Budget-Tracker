package com.example.BudgetTracker.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRequestDTO {
    private String email;

    private String password;

    private String fullName;

}

package com.example.BudgetTracker.Controller;

import com.example.BudgetTracker.DTO.UserRequestDTO;
import com.example.BudgetTracker.DTO.UserResponseDTO;
import com.example.BudgetTracker.Service.AuthService;
import com.example.BudgetTracker.Service.UserService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@Valid @RequestBody UserRequestDTO dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }
}
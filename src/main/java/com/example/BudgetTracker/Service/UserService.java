package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.DTO.AccountResponseDTO;
import com.example.BudgetTracker.DTO.UserRequestDTO;
import com.example.BudgetTracker.DTO.UserResponseDTO;
import com.example.BudgetTracker.Entity.Account;
import com.example.BudgetTracker.Entity.User;
import com.example.BudgetTracker.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Dependency Injection
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO register(UserRequestDTO dto) {

        boolean emailTaken = userRepository.findByEmail(dto.getEmail()).isPresent(); // Uses findByEmail in UserRepository.java

        if (emailTaken) {
            throw new RuntimeException("Email is already in use");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFullName(dto.getFullName());

        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
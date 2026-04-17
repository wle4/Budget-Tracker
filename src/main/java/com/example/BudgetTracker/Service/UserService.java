package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.Entity.User;
import com.example.BudgetTracker.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Dependency Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        boolean emailTaken = userRepository.findByEmail(user.getEmail()).isPresent(); // Uses findByEmail in UserRepository.java

        if (emailTaken) {
            throw new RuntimeException("Email is already in use");
        }
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
package com.example.BudgetTracker.Controller;

import com.example.BudgetTracker.Entity.User;
import com.example.BudgetTracker.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/register")
//    public User register(@RequestBody User user) {
//        return userService.register(user);
//    }

    @GetMapping("/find")
    public User findByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

}
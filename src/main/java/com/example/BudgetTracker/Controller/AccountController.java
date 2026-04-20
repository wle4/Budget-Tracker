package com.example.BudgetTracker.Controller;

import com.example.BudgetTracker.DTO.AccountRequestDTO;
import com.example.BudgetTracker.DTO.AccountResponseDTO;
import com.example.BudgetTracker.Service.AccountService;
import com.example.BudgetTracker.Service.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final JwtService jwt;

    public AccountController(AccountService accountService, JwtService jwt) {
        this.accountService = accountService;
        this.jwt = jwt;
    }

    @PostMapping
    public AccountResponseDTO createAccount(@RequestBody AccountRequestDTO dto,
                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwt.extractEmail(token);
        return accountService.createAccount(dto, email);
    }

    @GetMapping
    public List<AccountResponseDTO> getAccounts(@RequestParam UUID userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable UUID accountId) {
        accountService.deleteAccount(accountId);
    }
}

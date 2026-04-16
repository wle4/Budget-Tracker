package com.example.BudgetTracker.Controller;

import com.example.BudgetTracker.Entity.Account;
import com.example.BudgetTracker.Service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    public List<Account> getAccounts(@RequestParam UUID userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable UUID accountId) {
        accountService.deleteAccount(accountId);
    }
}

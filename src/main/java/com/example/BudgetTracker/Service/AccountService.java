package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.Entity.Account;
import com.example.BudgetTracker.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByUserId(UUID userId) {
        List<Account> accountList = accountRepository.findByUserId(userId);
        if (accountList.isEmpty()) {
            throw new RuntimeException("No accounts registered with this user");
        }
        return accountList;
    }

    public void deleteAccount(UUID accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.deleteById(accountId);
    }


}

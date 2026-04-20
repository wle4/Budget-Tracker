package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.DTO.AccountRequestDTO;
import com.example.BudgetTracker.DTO.AccountResponseDTO;
import com.example.BudgetTracker.Entity.Account;
import com.example.BudgetTracker.Entity.User;
import com.example.BudgetTracker.Repository.AccountRepository;
import com.example.BudgetTracker.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountResponseDTO createAccount(AccountRequestDTO dto, String email) {
        // look up the actual User object
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // send data from DTO to an Account object
        Account account = new Account();
        account.setName(dto.getName());
        account.setType(dto.getType());
        account.setBalance(dto.getBalance());
        account.setUser(user);

        Account saved = accountRepository.save(account); // save Account object to AccountRepository
        return toResponseDTO(saved); // convert Account to an AccountResponseDTO
    }

    public List<AccountResponseDTO> getAccountsByUserId(UUID userId) {
        List<Account> accountList = accountRepository.findByUserId(userId);
        if (accountList.isEmpty()) {
            throw new RuntimeException("No accounts registered with this user");
        }
        return accountList.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void deleteAccount(UUID accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        accountRepository.deleteById(accountId);
    }

    private AccountResponseDTO toResponseDTO(Account account) {
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setType(account.getType());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUserId(account.getUser().getId());
        return dto;
    }
}

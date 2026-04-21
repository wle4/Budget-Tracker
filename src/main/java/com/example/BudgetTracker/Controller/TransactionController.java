package com.example.BudgetTracker.Controller;

import com.example.BudgetTracker.DTO.TransactionRequestDTO;
import com.example.BudgetTracker.DTO.TransactionResponseDTO;
import com.example.BudgetTracker.Entity.Transaction;
import com.example.BudgetTracker.Service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransactionResponseDTO createTransaction(@Valid @RequestBody TransactionRequestDTO dto, @Valid @PathVariable UUID accountId) {
        return transactionService.createTransaction(dto, accountId);
    }

    @GetMapping
    public List<TransactionResponseDTO> getTransactions(@PathVariable UUID accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable UUID id) {
        transactionService.reverseTransaction(id);
    }

}
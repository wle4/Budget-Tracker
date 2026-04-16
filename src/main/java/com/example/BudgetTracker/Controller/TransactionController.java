package com.example.BudgetTracker.Controller;

import com.example.BudgetTracker.Entity.Transaction;
import com.example.BudgetTracker.Service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> getTransactions(@RequestParam UUID accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable UUID id) {
        transactionService.reverseTransaction(id);
    }

}
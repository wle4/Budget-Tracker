package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.Entity.TransactionStatus;
import com.example.BudgetTracker.Repository.TransactionRepository;
import com.example.BudgetTracker.Entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByAccountId(UUID accountId) {
        List<Transaction> transactionList = transactionRepository.findByAccountId(accountId);
        if (transactionList.isEmpty()) {
            throw new RuntimeException("No transactions associated by this account");
        }
        return transactionList;
    }

    public Transaction reverseTransaction(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() == TransactionStatus.REVERSED) {
            throw new RuntimeException("Transaction is already reversed");
        }
        transaction.setStatus(TransactionStatus.REVERSED);
        return transactionRepository.save(transaction);
    }


}

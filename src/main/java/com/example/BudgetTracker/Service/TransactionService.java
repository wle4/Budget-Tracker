package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.DTO.TransactionRequestDTO;
import com.example.BudgetTracker.DTO.TransactionResponseDTO;
import com.example.BudgetTracker.Entity.Account;
import com.example.BudgetTracker.Entity.Category;
import com.example.BudgetTracker.Entity.TransactionStatus;
import com.example.BudgetTracker.Repository.CategoryRepository;
import com.example.BudgetTracker.Repository.TransactionRepository;
import com.example.BudgetTracker.Repository.AccountRepository;
import com.example.BudgetTracker.Entity.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto, UUID accountId) {
        // look up the actual Account object
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // look up the actual Category object
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        // send data from DTO to a Transaction object
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());
        transaction.setStatus(dto.getStatus());

        Transaction saved = transactionRepository.save(transaction); // save Transaction object to TransactionRepository
        return toResponseDTO(saved); // convert Transaction to a TransactionResponseDTO
    }

//    public Transaction createTransaction(Transaction transaction) {
//        return transactionRepository.save(transaction);
//    }

    public List<TransactionResponseDTO> getTransactionsByAccountId(UUID accountID) {
        List<Transaction> transactionList = transactionRepository.findByAccountId(accountID);
        List<TransactionResponseDTO> dto = new ArrayList<>();

        if (transactionList.isEmpty()) {
            throw new RuntimeException("No transactions associated by this account");
        }

        return transactionList.stream()
                .map(this::toResponseDTO)
                .toList();
    }

//    public List<Transaction> getTransactionsByAccountId(UUID accountId) {
//        List<Transaction> transactionList = transactionRepository.findByAccountId(accountId);
//        if (transactionList.isEmpty()) {
//            throw new RuntimeException("No transactions associated by this account");
//        }
//        return transactionList;
//    }

    public Transaction reverseTransaction(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() == TransactionStatus.REVERSED) {
            throw new RuntimeException("Transaction is already reversed");
        }
        transaction.setStatus(TransactionStatus.REVERSED);
        return transactionRepository.save(transaction);
    }

    private TransactionResponseDTO toResponseDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setAccountId(transaction.getAccount().getId()); // TransactionResponseDTO only needs Account ID
        dto.setCategoryId(transaction.getCategory() != null ? transaction.getCategory().getId() : null); // TransactionResponseDTO only needs optional Category ID
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setStatus(transaction.getStatus());
        dto.setReversalOf(transaction.getReversalOf() != null ? transaction.getReversalOf().getId() : null); // TransactionResponseDTO only needs optional Transaction (reversalOf) ID
        dto.setCreatedAt(transaction.getCreatedAt());

        return dto;
    }

}

package com.example.BudgetTracker.Service;

import com.example.BudgetTracker.DTO.TransactionRequestDTO;
import com.example.BudgetTracker.DTO.TransactionResponseDTO;
import com.example.BudgetTracker.Entity.*;
import com.example.BudgetTracker.Repository.AccountRepository;
import com.example.BudgetTracker.Repository.CategoryRepository;
import com.example.BudgetTracker.Repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void createTransaction_ValidAccount_ReturnsTransactionResponseDTO() {
        // arrange
        Category category = new Category();
        category.setId(UUID.randomUUID());

        Account account = new Account();
        account.setId(UUID.randomUUID());

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setAmount(BigDecimal.valueOf(1000));
        transaction.setDescription("TestDescription");
        transaction.setStatus(TransactionStatus.PENDING);

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setAmount(BigDecimal.valueOf(1000));
        dto.setCategoryId(category.getId());
        dto.setDescription("TestDescription");
        dto.setStatus(TransactionStatus.PENDING);

        // tell
        when(accountRepository.findById(account.getId()))
                .thenReturn(Optional.of(account));

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        // act
        TransactionResponseDTO result = transactionService.createTransaction(dto, account.getId());

        // assert
        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(account.getId(), result.getAccountId());
        assertEquals(category.getId(), result.getCategoryId());
        assertEquals(BigDecimal.valueOf(1000), result.getAmount());
        assertEquals("TestDescription", result.getDescription());
        assertEquals(TransactionStatus.PENDING, result.getStatus());

        // verify
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    @Test
    void createTransaction_InvalidAccountId_ThrowsException() {
        // arrange
        UUID invalidId = UUID.randomUUID();

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setAmount(BigDecimal.valueOf(1000));
        dto.setStatus(TransactionStatus.PENDING);
        dto.setDescription("TestDescription");

        // tell
        when(accountRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // act + assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(dto, invalidId);
        });

        assertEquals("Account not found", exception.getMessage());

        // verify category and transaction were never reached
        verify(categoryRepository, never()).findById(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void createTransaction_InvalidCategoryId_ThrowsException() {
        // arrange
        UUID invalidCategoryId = UUID.randomUUID();

        Account savedAccount = new Account();
        savedAccount.setId(UUID.randomUUID());

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setAmount(BigDecimal.valueOf(1000));
        dto.setStatus(TransactionStatus.PENDING);
        dto.setDescription("TestDescription");
        dto.setCategoryId(invalidCategoryId);

        // tell
        when(accountRepository.findById(savedAccount.getId()))
                .thenReturn(Optional.of(savedAccount));

        when(categoryRepository.findById(invalidCategoryId))
                .thenReturn(Optional.empty());

        // act + assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(dto, savedAccount.getId());
        });

        assertEquals("Category not found", exception.getMessage());

        // verify
        verify(accountRepository, times(1)).findById(savedAccount.getId());
        verify(categoryRepository, times(1)).findById(invalidCategoryId);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void getTransactionsByAccountId_ValidAccountId_ReturnsTransactionResponseDTOList() {
        // arrange
        Account account = new Account();
        account.setId(UUID.randomUUID());

        Category category = new Category();
        category.setId(UUID.randomUUID());

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(BigDecimal.valueOf(1000));
        transaction.setDescription("TestDescription");
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCategory(category);

        List<Transaction> transactionList = List.of(transaction);

        // tell
        when(transactionRepository.findByAccountId(account.getId()))
                .thenReturn(transactionList);

        // act
        List<TransactionResponseDTO> result = transactionService.getTransactionsByAccountId(account.getId());

        // assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(transaction.getId(), result.get(0).getId());
        assertEquals(account.getId(), result.get(0).getAccountId());
        assertEquals(category.getId(), result.get(0).getCategoryId());
        assertEquals(BigDecimal.valueOf(1000), result.get(0).getAmount());
        assertEquals("TestDescription", result.get(0).getDescription());
        assertEquals(TransactionStatus.PENDING, result.get(0).getStatus());

        // verify
        verify(transactionRepository, times(1)).findByAccountId(account.getId());
    }

    @Test
    void getTransactionsByAccountId_InvalidAccountId_ReturnsTransactionResponseDTOList() {
        // arrange
        Account account = new Account();
        account.setId(UUID.randomUUID());

        // tell
        when(transactionRepository.findByAccountId(account.getId()))
                .thenReturn(List.of());

        // act + assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.getTransactionsByAccountId(account.getId());
        });

        //assert
        assertEquals("No transactions associated by this account", exception.getMessage());

        // verify
        verify(transactionRepository, times(1)).findByAccountId(account.getId());
    }

    @Test
    void reverseTransaction_ValidTransactionId_ReturnsTransaction() {
        // arrange
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setStatus(TransactionStatus.POSTED);

        // tell
        when(transactionRepository.findById(transaction.getId()))
                .thenReturn(Optional.of(transaction));

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        // act
        Transaction result = transactionService.reverseTransaction(transaction.getId());

        // assert
        assertNotNull(result);
        assertEquals(TransactionStatus.REVERSED, result.getStatus());
    }

    @Test
    void reverseTransaction_InvalidTransactionId_ThrowsException() {
        // arrange
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());

        // tell
        when(transactionRepository.findById(transaction.getId()))
                .thenReturn(Optional.empty());

        // act + assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.reverseTransaction(transaction.getId());
        });

        // assert
        assertEquals("Transaction not found", exception.getMessage());

        // verify
        verify(transactionRepository, times(1)).findById(transaction.getId());
    }

    @Test
    void reverseTransaction_TransactionAlreadyReversed_ThrowsException() {
        // arrange
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setStatus(TransactionStatus.REVERSED);

        // tell
        when(transactionRepository.findById(transaction.getId()))
                .thenReturn(Optional.of(transaction));

        // act + assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.reverseTransaction(transaction.getId());
        });

        // assert
        assertEquals("Transaction is already reversed", exception.getMessage());

        // verify
        verify(transactionRepository, times(1)).findById(transaction.getId());
    }
}
